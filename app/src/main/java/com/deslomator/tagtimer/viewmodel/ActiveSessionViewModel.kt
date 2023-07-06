package com.deslomator.tagtimer.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.ExportedSession
import com.deslomator.tagtimer.state.ActiveSessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class ActiveSessionViewModel @Inject constructor(
    private val appDao: AppDao, savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _sessionId = MutableStateFlow(0)
    private val _state = MutableStateFlow(ActiveSessionState())
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _events = _sessionId
        .flatMapLatest {
            appDao.getActiveEventsForSession(_sessionId.value)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _tags = appDao.getActiveTags()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _preSelectedTags = _sessionId
        .flatMapLatest {
            appDao.getPreSelectedTagsForSession(_sessionId.value)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _persons = appDao.getActivePersons()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _preSelectedPersons = _sessionId
        .flatMapLatest {
            appDao.getPreSelectedPersonsForSession(_sessionId.value)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _places = appDao.getActivePlaces()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _preSelectedPlaces = _sessionId
        .flatMapLatest {
            appDao.getPreSelectedPlacesForSession(_sessionId.value)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = combine(
        _state, _events, _preSelectedTags, _tags, _preSelectedPersons, _persons,
        _preSelectedPlaces, _places
    ) { state, events, preSelectedTags, tags, preSelectedPersons, persons,
        preSelectedPlaces, places ->
        state.copy(
            events = events,
            preSelectedTags = preSelectedTags,
            tags = tags,
            preSelectedPersons = preSelectedPersons,
            persons = persons,
            preSelectedPlaces = preSelectedPlaces,
            places = places,
        )
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), ActiveSessionState()
    )

    fun onAction(action: ActiveSessionAction) {
        when(action) {
            is ActiveSessionAction.PreSelectedTagClicked -> {
                viewModelScope.launch {
                    val event = Event(
                        sessionId = _sessionId.value,
                        elapsedTimeMillis = action.cursor,
                        tag = action.tag.name,
                        person = state.value.currentPersonName,
                        place = state.value.currentPlaceName,
                        color = action.tag.color,
                    )
                    val id = appDao.upsertEvent(event)
                    _state.update {
                        it.copy( eventForScrollTo = event.copy(id = id.toInt()))
                    }
                }
            }
            is ActiveSessionAction.UpdateSession -> {
                val session = state.value.currentSession.copy(
                    durationMillis = getSessionDuration(),
                    eventCount = state.value.events.size
                )
                viewModelScope.launch { appDao.upsertSession(session) }
            }
            is ActiveSessionAction.TrashEventSwiped -> {
                viewModelScope.launch {
                    // we don't want the Event that was retrieved
                    // in the action because it was stale
                    // get the updated one from the DB instead
                    val event = appDao.getEvent(action.event.id)
                    val trashed = event.copy(inTrash = true)
                    appDao.upsertEvent(trashed) }
            }
            is ActiveSessionAction.EventClicked -> {
                _state.update { it.copy(
                    eventForDialog = action.event,
                    showEventEditionDialog = true
                ) }
            }
            is ActiveSessionAction.AcceptEventEditionClicked -> {
                viewModelScope.launch { appDao.upsertEvent(action.event) }
                /*
                 state takes some time to update after upserting the event;
                 workaround: we take the updated event out of the list and
                 compare it with the rest of the list to set the new duration
                */
                val maxInList = state.value.events
                    .filter { it.id != action.event.id }
                    .maxOfOrNull { it.elapsedTimeMillis } ?: 0
                val duration = maxOf(maxInList, action.event.elapsedTimeMillis)
                val session = state.value.currentSession.copy(durationMillis = duration)
                _state.update {
                    it.copy(
                        currentSession = session,
                        showEventEditionDialog = false,
                        eventForScrollTo = action.event
                    )
                }
            }
            ActiveSessionAction.DismissEventEditionDialog -> {
                _state.update { it.copy(showEventEditionDialog = false) }
            }
            ActiveSessionAction.ExportSessionClicked -> {
                exportSession()
            }
            ActiveSessionAction.SessionExported -> {
                _state.update { it.copy(exportData = false) }
            }
            is ActiveSessionAction.TimeClicked -> {
                val s = state.value.currentSession.copy(
                    durationMillis = getSessionDuration()
                )
                _state.update { it.copy(
                    currentSession = s,
                    showTimeDialog = true
                ) }
            }
            is ActiveSessionAction.DismissTimeDialog -> {
                Log.d(TAG, "ActiveSessionAction.DismissTimeDialog")
                _state.update { it.copy(showTimeDialog = false) }
            }
            is ActiveSessionAction.PreSelectedPersonClicked -> {
                val person = if (action.personName == state.value.currentPersonName) ""
                else action.personName
                _state.update { it.copy(currentPersonName = person) }
            }
            is ActiveSessionAction.PreSelectedPlaceClicked -> {
                val place = if (action.placeName == state.value.currentPlaceName) ""
                else action.placeName
                _state.update { it.copy(currentPlaceName = place) }
            }
        }
    }

    private fun exportSession() {
        val json = Json.encodeToString(
            ExportedSession(state.value.currentSession, state.value.events)
        )
        _state.update { it.copy(
            dataToExport = json,
            exportData = true
        ) }
    }

    private fun getSessionDuration(): Long {
        return state.value.events
            .maxOfOrNull { it.elapsedTimeMillis } ?: 0
    }

    fun updateId(id: Int) {
        Log.d(TAG, "updateId($id)")
        _sessionId.update { id }
        viewModelScope.launch {
            val cur = appDao.getSession(id)
            val s = cur.copy(
                lastAccessMillis = System.currentTimeMillis(),
                durationMillis = getSessionDuration()
            )
            _state.update {
                it.copy(currentSession = s)
            }
            appDao.upsertSession(s)
            if (state.value.events.isNotEmpty())
                _state.update { it.copy(eventForScrollTo = state.value.events.last()) }
        }
    }

    private inline fun <T1, T2, T3, T4, T5, T6, T7, T8, R> combine(
        flow1: Flow<T1>,
        flow2: Flow<T2>,
        flow3: Flow<T3>,
        flow4: Flow<T4>,
        flow5: Flow<T5>,
        flow6: Flow<T6>,
        flow7: Flow<T7>,
        flow8: Flow<T8>,
        crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8) -> R
    ): Flow<R> {
        return combine(flow1, flow2, flow3, flow4, flow5, flow6, flow7, flow8) { args: Array<*> ->
            @Suppress("UNCHECKED_CAST")
            transform(
                args[0] as T1,
                args[1] as T2,
                args[2] as T3,
                args[3] as T4,
                args[4] as T5,
                args[5] as T6,
                args[6] as T7,
                args[7] as T8,
            )
        }
    }

    init {
        val sessionId = savedStateHandle.get<Int>("sessionId") ?: 0
        _sessionId.update { sessionId }
        viewModelScope.launch {
            val cur = appDao.getSession(sessionId)
            val s = cur.copy(
                lastAccessMillis = System.currentTimeMillis(),
                durationMillis = getSessionDuration()
            )
            _state.update {
                it.copy(currentSession = s)
            }
            appDao.upsertSession(s)
            if (state.value.events.isNotEmpty())
                _state.update { it.copy(eventForScrollTo = state.value.events.last()) }
        }
    }

    companion object {
        private const val TAG = "ActiveSessionViewModel"
    }
}
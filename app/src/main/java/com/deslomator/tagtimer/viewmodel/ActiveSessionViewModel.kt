package com.deslomator.tagtimer.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.ExportedSession
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Preselected
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.util.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class ActiveSessionViewModel @Inject constructor(
    private val appDao: AppDao, savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _sessionId = MutableStateFlow(0L)
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
                        it.copy( eventForScrollTo = event.copy(id = id))
                    }
                }
            }
            is ActiveSessionAction.ExitSession -> {
                val time = System.currentTimeMillis()
                val session = state.value.currentSession.copy(
                    lastAccessMillis = time,
                    durationMillis = getSessionDuration(action.cursor),
                    eventCount = state.value.events.size,
                    startTimestampMillis =
                    if (action.isRunning) time - action.cursor else 0
                )
                viewModelScope.launch { appDao.upsertSession(session) }
            }
            is ActiveSessionAction.TrashEventSwiped -> {
                viewModelScope.launch {
                    // we don't want the Event that was retrieved
                    // in the action because it was stale
                    // get the updated one from the DB instead
                    val event = appDao.getEvent(action.event.id!!)
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
                runBlocking {
                    viewModelScope.launch { appDao.upsertEvent(action.event) }
                }
                val maxInList = state.value.events
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
                createNewLabels(action.event)
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
                    durationMillis = getSessionDuration(action.cursor)
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

    private fun getSessionDuration(cursor: Long): Long {
        return maxOf(
            cursor,
            state.value.events.maxOfOrNull { it.elapsedTimeMillis } ?: 0
        )
    }

    /**
    create new Labels if an Event edition resulted in non existent label names
     */
    private fun createNewLabels(event: Event) {
        if (!_persons.value.map { it.name }.contains(event.person)) {
            viewModelScope.launch {
                val newId = appDao.upsertPerson(
                    Label.Person(
                        name = event.person,
                        color = event.color
                    )
                )
                appDao.upsertPreSelectedPerson(
                    Preselected.Person(
                        sessionId = state.value.currentSession.id!!,
                        labelId = newId
                    )
                )
            }
        }
        if (!_places.value.map { it.name }.contains(event.place)) {
            viewModelScope.launch {
                val newId = appDao.upsertPlace(
                    Label.Place(
                        name = event.place,
                        color = event.color
                    )
                )
                appDao.upsertPreSelectedPlace(
                    Preselected.Place(
                        sessionId = state.value.currentSession.id!!,
                        labelId = newId
                    )
                )
            }
        }
        if (!_tags.value.map { it.name }.contains(event.tag)) {
            viewModelScope.launch {
                val newId = appDao.upsertTag(
                    Label.Tag(
                        name = event.tag,
                        color = event.color
                    )
                )
                appDao.upsertPreSelectedTag(
                    Preselected.Tag(
                        sessionId = state.value.currentSession.id!!,
                        labelId = newId
                    )
                )
            }
        }
    }

    init {
        val sessionId = savedStateHandle.get<Long>("sessionId") ?: 0
        _sessionId.update { sessionId }
        viewModelScope.launch {
            _state.update {
                it.copy(currentSession = appDao.getSession(sessionId))
            }
            if (state.value.events.isNotEmpty())
                _state.update { it.copy(eventForScrollTo = state.value.events.last()) }
        }
    }

    companion object {
        private const val TAG = "ActiveSessionViewModel"
    }
}
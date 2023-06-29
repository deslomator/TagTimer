package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.ExportedEvent
import com.deslomator.tagtimer.model.ExportedSession
import com.deslomator.tagtimer.model.PreSelectedPerson
import com.deslomator.tagtimer.model.PreSelectedPlace
import com.deslomator.tagtimer.model.PreSelectedTag
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.toDateTime
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
    private val appDao: AppDao,
): ViewModel() {

    private val _sessionId = MutableStateFlow(0)
    private val _state = MutableStateFlow(ActiveSessionState())
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _events = _sessionId
        .flatMapLatest {
            appDao.getActiveEventsForSession(_sessionId.value)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _trashedEvents = _sessionId
        .flatMapLatest {
            appDao.getTrashedEventsForSession(_sessionId.value)
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
        _preSelectedPlaces, _places, _trashedEvents
    ) { state, events, preSelectedTags, tags, preSelectedPersons, persons,
        preSelectedPlaces, places, trashedEvents ->
        state.copy(
            events = events,
            preSelectedTags = preSelectedTags,
            tags = tags,
            preSelectedPersons = preSelectedPersons,
            persons = persons,
            preSelectedPlaces = preSelectedPlaces,
            places = places,
            trashedEvents = trashedEvents,
        )
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), ActiveSessionState()
    )

    fun onAction(action: ActiveSessionAction) {
        when(action) {
            is ActiveSessionAction.UpdateSessionId -> {
                _sessionId.update { action.id }
                viewModelScope.launch {
                    val cur = appDao.getSession(action.id)
                    val s = cur.copy(
                        lastAccessMillis = System.currentTimeMillis(),
                        durationMillis = getSessionDuration()
                    )
                    _state.update {
                        it.copy(currentSession = s)
                    }
                    appDao.upsertSession(s)
                }
            }
            is ActiveSessionAction.PlayPauseClicked -> {
                _state.update { it.copy(isRunning = !state.value.isRunning) }
            }
            is ActiveSessionAction.SelectTagsClicked -> {
                _state.update { it.copy( showTagsDialog = true) }
            }
            is ActiveSessionAction.DismissTagDialog -> {
                _state.update { it.copy( showTagsDialog = false) }
            }
            is ActiveSessionAction.SelectTagCheckedChange -> {
                if (action.checked) {
                    val pst = PreSelectedTag (
                        sessionId = state.value.currentSession.id,
                        tagId = action.tagId
                    )
                    viewModelScope.launch { appDao.upsertPreSelectedTag(pst) }
                } else {
                    val psTag = state.value.preSelectedTags
                        .firstOrNull { it.tagId == action.tagId }
                    psTag?.let { viewModelScope.launch { appDao.deletePreSelectedTag(it) } }
                }
            }
            is ActiveSessionAction.SelectPersonCheckedChange -> {
                if (action.checked) {
                    val pst = PreSelectedPerson (
                        sessionId = state.value.currentSession.id,
                        personId = action.personId
                    )
                    viewModelScope.launch { appDao.upsertPreSelectedPerson(pst) }
                } else {
                    val psPerson = state.value.preSelectedPersons
                        .firstOrNull { it.personId == action.personId }
                    psPerson?.let { viewModelScope.launch { appDao.deletePreSelectedPerson(it) } }
                }
            }
            is ActiveSessionAction.SelectPlaceCheckedChange -> {
                if (action.checked) {
                    val psPlace = PreSelectedPlace (
                        sessionId = state.value.currentSession.id,
                        placeId = action.placeId
                    )
                    viewModelScope.launch { appDao.upsertPreSelectedPlace(psPlace) }
                } else {
                    val pst = state.value.preSelectedPlaces
                        .firstOrNull { it.placeId == action.placeId }
                    pst?.let { viewModelScope.launch { appDao.deletePreSelectedPlace(it) } }
                }
            }
            ActiveSessionAction.AcceptTagSelectionClicked -> {
                _state.update { it.copy( showTagsDialog = false) }
            }
            is ActiveSessionAction.PreSelectedTagClicked -> {
                if (state.value.isRunning) {
                    val event = Event(
                        sessionId = _sessionId.value,
                        elapsedTimeMillis = state.value.cursor,
                        category = action.tag.category,
                        label = action.tag.label,
                        color = action.tag.color,
                        person = state.value.currentPersonName,
                        place = state.value.currentPlaceName,
                    )
                    viewModelScope.launch { appDao.upsertEvent(event) }
                }
            }
            is ActiveSessionAction.StopSession -> {
                _state.update { it.copy(isRunning = false) }
                val session = state.value.currentSession.copy(
                    durationMillis = getSessionDuration(),
                    eventCount = state.value.events.size
                )
                viewModelScope.launch { appDao.upsertSession(session) }
            }
            ActiveSessionAction.EventTrashClicked -> {
                _state.update { it.copy( showEventTrash = true) }
            }
            ActiveSessionAction.DismissEventTrashDialog -> {
                _state.update { it.copy( showEventTrash = false) }
            }
            is ActiveSessionAction.DeleteEventClicked -> {
                viewModelScope.launch { appDao.deleteEvent(action.event) }
            }
            is ActiveSessionAction.RestoreEventClicked -> {
                viewModelScope.launch {
                    val e = action.event.copy(inTrash = false)
                    appDao.upsertEvent(e) }
            }
            is ActiveSessionAction.TrashEventSwiped -> {
                viewModelScope.launch {
                    // we don't want the Event that was retrieved
                    // in the action because it was stale
                    // get the updated one from the DB instead
                    val event = appDao.getEventById(action.eventId)
                    val trashed = event.copy(inTrash = true)
                    appDao.upsertEvent(trashed) }
            }
            ActiveSessionAction.EditSessionClicked -> {
                _state.update { it.copy(showSessionEditionDialog = true) }
            }
            is ActiveSessionAction.AcceptSessionEditionClicked -> {
                _state.update { it.copy(
                    showSessionEditionDialog = false,
                    currentSession = action.session
                ) }
                viewModelScope.launch { appDao.upsertSession(action.session) }
            }
            ActiveSessionAction.DismissSessionEditionDialog -> {
                _state.update { it.copy(showSessionEditionDialog = false) }
            }
            is ActiveSessionAction.EventClicked -> {
                _state.update { it.copy(
                    currentEvent = action.event,
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
                        showEventEditionDialog = false
                    )
                }
            }
            ActiveSessionAction.DismissEventEditionDialog -> {
                _state.update { it.copy(showEventEditionDialog = false) }
            }
            is ActiveSessionAction.EventInTrashClicked -> {
                _state.update { it.copy(
                    currentEvent = action.event,
                    showEventInTrashDialog = true
                ) }
            }
            is ActiveSessionAction.DismissEventInTrashDialog -> {
                _state.update { it.copy(showEventInTrashDialog = false) }
            }
            ActiveSessionAction.ExportSessionClicked -> {
                exportSession()
            }
            ActiveSessionAction.SessionExported -> {
                _state.update { it.copy(exportSession = false) }
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
            is ActiveSessionAction.SetCursor -> {
                _state.update { it.copy(cursor = action.time) }
            }
            is ActiveSessionAction.IncreaseCursor -> {
                val newTime = state.value.cursor + action.stepMillis
                _state.update { it.copy(cursor = newTime) }
            }
            is ActiveSessionAction.DismissTimeDialog -> {
                _state.update { it.copy(showTimeDialog = false) }
            }
            is ActiveSessionAction.LabelTypeSelected -> {
                _state.update { it.copy(currentLabel = action.type) }
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
        val s = state.value.currentSession
        val exported = ExportedSession(
            date = s.lastAccessMillis.toDateTime(),
            name = s.name,
            durationSecs = (s.durationMillis / 1000).toInt(),
            events = state.value.events.map {
                ExportedEvent(
                    label = it.label,
                    category = it.category,
                    note = it.note,
                    person = it.person,
                    place = it.place,
                    elapsedTimeSeconds = (it.elapsedTimeMillis / 1000).toInt(),
                )
            }
        )
        val json = Json.encodeToString(exported)
        _state.update {
            it.copy(
                sessionToExport = json,
                exportSession = true
            )
        }
    }

    private fun getSessionDuration(): Long {
        return state.value.events
            .maxOfOrNull { it.elapsedTimeMillis } ?: 0
    }

    inline fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> combine(
        flow: Flow<T1>,
        flow2: Flow<T2>,
        flow3: Flow<T3>,
        flow4: Flow<T4>,
        flow5: Flow<T5>,
        flow6: Flow<T6>,
        flow7: Flow<T7>,
        flow8: Flow<T8>,
        flow9: Flow<T9>,
        crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8, T9) -> R
    ): Flow<R> {
        return combine(flow, flow2, flow3, flow4, flow5, flow6, flow7, flow8, flow9) { args: Array<*> ->
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
                args[8] as T9,
            )
        }
    }

    companion object {
        private const val TAG = "ActiveSessionViewModel"
    }
}
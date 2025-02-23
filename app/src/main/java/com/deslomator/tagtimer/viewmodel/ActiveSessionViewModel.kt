package com.deslomator.tagtimer.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Preselected
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.util.combine
import com.deslomator.tagtimer.util.toCsv
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
                        sessionId = _sessionId.value, // the value in state can be null
                        elapsedTimeMillis = action.elapsed,
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
                    durationMillis = getSessionDuration(),
                    eventCount = state.value.events.size,
                )
                viewModelScope.launch { appDao.upsertSession(session) }
                Log.d(TAG, "exitSession, running: ${session.running}")
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
            ActiveSessionAction.ShareSessionClicked -> {
                _state.update { it.copy(
                    dataToShare = state.value.events.toCsv(state.value.currentSession),
                    shareData = true
                ) }
            }
            ActiveSessionAction.SessionShared -> {
                _state.update { it.copy(shareData = false) }
            }
            is ActiveSessionAction.TimeClicked -> {
                _state.update { it.copy(
                    showTimeDialog = true
                ) }
            }
            is ActiveSessionAction.AcceptTimeDialog -> {
                val s = state.value.currentSession
                val newCursor = s.startTimestampMillis + action.offset
                val updated = s.copy(startTimestampMillis = newCursor)
                _state.update { it.copy(
                    currentSession = updated,
                    showTimeDialog = false
                ) }
            }
            is ActiveSessionAction.DismissTimeDialog -> {
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
            is ActiveSessionAction.PlayPauseClicked -> {
                val s =state.value.currentSession
                val d = getSessionDuration()
                if (s.running) {
                    val updated = s.copy(
                        running = false,
                        durationMillis = d,
                    )
                    _state.update { it.copy(currentSession = updated) }
                } else {
                    val updated = s.copy(
                        running = true,
                        startTimestampMillis = System.currentTimeMillis() - d
                    )
                    _state.update { it.copy(currentSession = updated) }
                }
            }
        }
    }

    /**
    create new Labels if an Event edition resulted in non existent label names
     */
    private fun createNewLabels(event: Event) {
        if (!_persons.value.map { it.name }.contains(event.person)) {
            viewModelScope.launch {
                val newPerson = Label.Person(
                    name = event.person,
                    color = event.color
                )
                appDao.upsertPerson(newPerson)
                appDao.upsertPreSelectedPerson(
                    Preselected.Person(
                        sessionId = state.value.currentSession.id!!,
                        labelId = newPerson.id
                    )
                )
            }
        }
        if (!_places.value.map { it.name }.contains(event.place)) {
            viewModelScope.launch {
                val newPlace = Label.Place(
                    name = event.place,
                    color = event.color
                )
                appDao.upsertPlace(newPlace)
                appDao.upsertPreSelectedPlace(
                    Preselected.Place(
                        sessionId = state.value.currentSession.id!!,
                        labelId = newPlace.id
                    )
                )
            }
        }
        if (!_tags.value.map { it.name }.contains(event.tag)) {
            viewModelScope.launch {
                val newTag = Label.Tag(
                    name = event.tag,
                    color = event.color
                )
                appDao.upsertTag(newTag)
                appDao.upsertPreSelectedTag(
                    Preselected.Tag(
                        sessionId = state.value.currentSession.id!!,
                        labelId = newTag.id
                    )
                )
            }
        }
    }

    private fun getSessionDuration(): Long {
        val s = state.value.currentSession
        return when {
            s.running -> System.currentTimeMillis() - s.startTimestampMillis
            else -> s.durationMillis
        }
    }

    private suspend fun updateDuration() {
        while (true) {
            val s = state.value.currentSession
            if (s.running) {
                val updated = s.copy(
                    durationMillis = getSessionDuration()
                )
                _state.update { it.copy(currentSession = updated) }
            }
            delay(1000)
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
            val s = state.value.currentSession
            if (s.running) {
                val updated = s.copy(
                    startTimestampMillis = System.currentTimeMillis() - s.durationMillis
                )
                _state.update { it.copy( currentSession = updated) }
            }
            // TODO check updateDuration() is executed last
            updateDuration()
        }
    }

    companion object {
        private const val TAG = "ActiveSessionViewModel"
    }
}
package com.deslomator.tagtimer.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.ancillary.PreferenceProvider
import com.deslomator.tagtimer.model.type.LabelSort
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.ui.theme.hue
import com.deslomator.tagtimer.util.combine
import com.deslomator.tagtimer.util.toColor
import com.deslomator.tagtimer.util.toCsv
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActiveSessionViewModel(
    private val appDao: AppDao,
    private val sessionId: Long,
) : ViewModel() {

    private val _state = MutableStateFlow(ActiveSessionState())

    private val _prefs = appDao.getPreferences()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _prefProvider = _prefs.mapLatest { prefs ->
        PreferenceProvider(prefs)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), PreferenceProvider())

    private val _eventsForDisplay = appDao.getEventsForDisplay(sessionId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _selectedTags = _prefProvider.flatMapLatest { prefProvider ->
        if (prefProvider.tagSort() == LabelSort.NAME) appDao.getSelectedTagsForSession(sessionId)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getSelectedTagsForSession(sessionId)
            .map { lst -> lst.sortedBy { it.color.toColor().hue() } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _selectedPersons = _prefProvider.flatMapLatest { prefProvider ->
        if (prefProvider.personSort() == LabelSort.NAME) appDao.getSelectedPersonsForSession(sessionId)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getSelectedPersonsForSession(sessionId)
            .map { lst -> lst.sortedBy { it.color.toColor().hue() }
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _selectedPlaces = _prefProvider.flatMapLatest { prefProvider ->
        if (prefProvider.placeSort() == LabelSort.NAME) appDao.getSelectedPlacesForSession(sessionId)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getSelectedPlacesForSession(sessionId)
            .map { lst -> lst.sortedBy { it.color.toColor().hue() }
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = combine(
        _state, _eventsForDisplay, _selectedTags, _selectedPersons,
        _selectedPlaces
    ) { state, eventsForDisplay, selectedTags, selectedPersons,
        selectedPlaces ->
        state.copy(
            eventsForDisplay = eventsForDisplay,
            selectedTags = selectedTags,
            selectedPersons = selectedPersons,
            selectedPlaces = selectedPlaces,
        )
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), ActiveSessionState()
    )

    fun onAction(action: ActiveSessionAction) {
        when (action) {
            is ActiveSessionAction.SelectedTagClicked -> {
                viewModelScope.launch {
                    val event = Event(
                        sessionId = sessionId,
                        elapsedTimeMillis = getSessionDuration(),
                        color = action.tag.color,
                        tagId = action.tag.id,
                        personId = state.value.currentPerson?.id,
                        placeId = state.value.currentPlace?.id,
                    )
                    val id = async(Dispatchers.IO) { appDao.upsertEvent(event) }.await()
                    scrollToIndex(id)
                }
            }

            is ActiveSessionAction.ExitSession -> {
                val time = System.currentTimeMillis()
                val session = state.value.currentSession.copy(
                    lastAccessMillis = time,
                    durationMillis = getSessionDuration(),
                    eventCount = state.value.eventsForDisplay.size,
                )
                viewModelScope.launch(Dispatchers.IO) { appDao.upsertSession(session) }
            }

            is ActiveSessionAction.TrashEventSwiped -> {
                viewModelScope.launch(Dispatchers.IO) {
                    appDao.trashEvent(action.event4d.event.id!!)
                }
            }

            is ActiveSessionAction.EventClicked -> {
                _state.update {
                    it.copy(
                        eventForDialog = action.event4d,
                        showEventEditionDialog = true
                    )
                }
            }

            is ActiveSessionAction.AcceptEventEditionClicked -> {
                _state.update { it.copy(showEventEditionDialog = false) }
                viewModelScope.launch {
                    val event = action.event4d.event
                    val job = launch(Dispatchers.IO) { appDao.upsertEvent(event) }
                    job.join()
                    // upsertEvent() returns -1 instead of the index on
                    // successful update, so we pass event.id to scrollToIndex()
                    scrollToIndex(event.id)
                }
            }

            ActiveSessionAction.DismissEventEditionDialog -> {
                _state.update { it.copy(showEventEditionDialog = false) }
            }

            ActiveSessionAction.ShareSessionClicked -> {
                _state.update {
                    it.copy(
                        dataToShare = state.value.eventsForDisplay.toCsv(state.value.currentSession),
                        shareData = true
                    )
                }
            }

            ActiveSessionAction.SessionShared -> {
                _state.update { it.copy(shareData = false) }
            }

            is ActiveSessionAction.TimeClicked -> {
                _state.update {
                    it.copy(
                        showTimeDialog = true
                    )
                }
            }

            is ActiveSessionAction.AcceptTimeDialog -> {
                val s = state.value.currentSession
                val updated = s.copy(
                    startTimestampMillis = System.currentTimeMillis() - action.newTime,
                    durationMillis = action.newTime
                )
                _state.update {
                    it.copy(
                        currentSession = updated,
                        showTimeDialog = false
                    )
                }
            }

            is ActiveSessionAction.DismissTimeDialog -> {
                _state.update { it.copy(showTimeDialog = false) }
            }

            is ActiveSessionAction.SelectedPersonClicked -> {
                val person = if (action.person == state.value.currentPerson) null
                else action.person
                _state.update { it.copy(currentPerson = person) }
            }

            is ActiveSessionAction.SelectedPlaceClicked -> {
                val place = if (action.place == state.value.currentPlace) null
                else action.place
                _state.update { it.copy(currentPlace = place) }
            }

            is ActiveSessionAction.PlayPauseClicked -> {
                val s = state.value.currentSession
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
     * we retrieve a current list of event ids from the database
     * because we can't rely on state.value.eventsForDisplay
     * to be updated quickly enough after upserting an Event
     */
    private suspend fun scrollToIndex(eventId: Long? = null) {
        withContext(Dispatchers.Default) {
            val eventIds = async(Dispatchers.IO) {
                appDao.getEventsForDisplayList(sessionId).map { it.event.id }
            }.await()
            val index = if (eventIds.isEmpty()) {
                null
            } else if (eventId != null) {
                eventIds.indexOf(eventId)
            } else {
                eventIds.lastIndex
            }
            _state.update { it.copy(indexForScrollTo = index) }
        }
    }

    private fun getSessionDuration(): Long {
        val s = state.value.currentSession
        return when {
            s.running -> System.currentTimeMillis() - s.startTimestampMillis
            else -> s.durationMillis
        }
    }

    private suspend fun runTimer() {
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

        viewModelScope.launch {
            val s = async(Dispatchers.IO) { appDao.getSession(sessionId) }.await()
            _state.update {
                it.copy(currentSession = s)
            }
            if (s.running) {
                val updated = s.copy(
                    startTimestampMillis = System.currentTimeMillis() - s.durationMillis
                )
                _state.update { it.copy(currentSession = updated) }
            }
            launch { runTimer() }
            launch { scrollToIndex() }
        }
    }

    companion object {
        private const val TAG = "ActiveSessionViewModel"
    }
}

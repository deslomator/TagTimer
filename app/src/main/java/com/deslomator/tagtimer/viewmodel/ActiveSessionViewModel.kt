package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.type.LabelSort
import com.deslomator.tagtimer.model.type.LabelType
import com.deslomator.tagtimer.model.type.PreferenceProvider
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.ui.theme.hue
import com.deslomator.tagtimer.util.combine
import com.deslomator.tagtimer.util.toColor
import com.deslomator.tagtimer.util.toCsv
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ActiveSessionViewModel(
    private val appDao: AppDao,
    private val sessionId: Long,
) : ViewModel() {

    private val _state = MutableStateFlow(ActiveSessionState())

    private val _prefs = appDao.getPreferences()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _prefProvider = _prefs.mapLatest { prefs ->
        PreferenceProvider(prefs)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PreferenceProvider())

    private val _eventsForDisplay = appDao.getEventsForDisplay(sessionId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _selectedTags = _prefProvider.flatMapLatest { prefProvider ->
        if (prefProvider.tagSort() == LabelSort.NAME) appDao.getSelectedLabelsForSession(sessionId, LabelType.TAG)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getSelectedLabelsForSession(sessionId, LabelType.TAG)
            .map { lst -> lst.sortedBy { it.color.toColor().hue() } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _selectedPersons = _prefProvider.flatMapLatest { prefProvider ->
        if (prefProvider.personSort() == LabelSort.NAME) appDao.getSelectedLabelsForSession(sessionId, LabelType.PERSON)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getSelectedLabelsForSession(sessionId, LabelType.PERSON)
            .map { lst -> lst.sortedBy { it.color.toColor().hue() }
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _selectedPlaces = _prefProvider.flatMapLatest { prefProvider ->
        if (prefProvider.placeSort() == LabelSort.NAME) appDao.getSelectedLabelsForSession(sessionId, LabelType.PLACE)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getSelectedLabelsForSession(sessionId, LabelType.PLACE)
            .map { lst -> lst.sortedBy { it.color.toColor().hue() }
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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
                    val id = appDao.upsertEvent(event)
                    // TODO probably not the right way of waiting for _eventsForDisplay to update
                    while (_eventsForDisplay.value.firstOrNull { it.event.id == id } == null) {
                        delay(5)
                    }
                    _state.update { sessionState ->
                        sessionState.copy(eventForScrollTo = _eventsForDisplay.value.first { it.event.id == id })
                    }
                }
            }

            is ActiveSessionAction.ExitSession -> {
                val time = System.currentTimeMillis()
                val session = state.value.currentSession.copy(
                    lastAccessMillis = time,
                    durationMillis = getSessionDuration(),
                    eventCount = state.value.eventsForDisplay.size,
                )
                viewModelScope.launch { appDao.upsertSession(session) }
            }

            is ActiveSessionAction.TrashEventSwiped -> {
                viewModelScope.launch {
                    val e = action.event4d.event.copy(inTrash = true)
                    appDao.upsertEvent(e)
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
            // the swipeable list item doesn't update when its child event item does,
            // so we get an stale Event when swiping it. The solution is to
            // first remove the item from the list and then insert it
            // that's what updateEventForlist() does
            is ActiveSessionAction.AcceptEventEditionClicked -> {
                viewModelScope.launch {
//                    appDao.upsertEvent(action.event4d.event)
                    appDao.updateEventForList(action.event4d.event)
                    _state.update {
                        it.copy(
                            showEventEditionDialog = false,
                            eventForScrollTo = action.event4d,
                        )
                    }
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

        viewModelScope.launch {
            _state.update {
                it.copy(currentSession = appDao.getSession(sessionId))
            }
            if (state.value.eventsForDisplay.isNotEmpty())
                _state.update { it.copy(eventForScrollTo = state.value.eventsForDisplay.last()) }
            val s = state.value.currentSession
            if (s.running) {
                val updated = s.copy(
                    startTimestampMillis = System.currentTimeMillis() - s.durationMillis
                )
                _state.update { it.copy(currentSession = updated) }
            }
            updateDuration()
        }
    }

    companion object {
        private const val TAG = "ActiveSessionViewModel"
    }
}

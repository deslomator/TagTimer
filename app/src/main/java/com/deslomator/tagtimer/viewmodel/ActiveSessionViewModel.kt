package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.type.LabelSort
import com.deslomator.tagtimer.model.type.PrefKey
import com.deslomator.tagtimer.model.type.SessionSort
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.ui.theme.hue
import com.deslomator.tagtimer.util.combine
import com.deslomator.tagtimer.util.toColor
import com.deslomator.tagtimer.util.toCsv
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
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
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ActiveSessionViewModel @Inject constructor(
    private val appDao: AppDao, savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _sessionId = MutableStateFlow(0L)
    private val _state = MutableStateFlow(ActiveSessionState())

    private val _tagSort = SortProvider.getTagSort(appDao, viewModelScope)
    private val _personSort = SortProvider.getPersonSort(appDao, viewModelScope)
    private val _placeSort = SortProvider.getPlaceSort(appDao, viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _events = _sessionId
        .flatMapLatest {
            appDao.getActiveEventsForSession(_sessionId.value)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _eventsForDisplay = _sessionId
        .flatMapLatest {
            appDao.getEventsForDisplay(_sessionId.value)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _tags = _tagSort.flatMapLatest { sort ->
        if (sort == LabelSort.NAME) appDao.getActiveTags()
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getActiveTags()
            .map { lst -> lst.sortedBy { it.color.toColor().hue() } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _preSelectedTags = _tagSort.flatMapLatest { sort ->
        if (sort == LabelSort.NAME) appDao.getPreSelectedTagsForSession(_sessionId.value)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getPreSelectedTagsForSession(_sessionId.value)
            .map { lst -> lst.sortedBy { it.color.toColor().hue() } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _persons = _personSort.flatMapLatest { sort ->
        if (sort == LabelSort.NAME) appDao.getActivePersons()
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getActivePersons().map { lst ->
            lst.sortedBy { it.color.toColor().hue() }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _preSelectedPersons = _personSort.flatMapLatest { sort ->
        if (sort == LabelSort.NAME) appDao.getPreSelectedPersonsForSession(_sessionId.value)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getPreSelectedPersonsForSession(_sessionId.value).map { lst ->
            lst.sortedBy { it.color.toColor().hue() }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _places = _placeSort.flatMapLatest { sort ->
        if (sort == LabelSort.NAME) appDao.getActivePLaces()
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getActivePLaces().map { lst ->
            lst.sortedBy { it.color.toColor().hue() }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _preSelectedPlaces = _placeSort.flatMapLatest { sort ->
        if (sort == LabelSort.NAME) appDao.getPreSelectedPlacesForSession(_sessionId.value)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getPreSelectedPlacesForSession(_sessionId.value).map { lst ->
            lst.sortedBy { it.color.toColor().hue() }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val state = combine(
        _state, _events, _eventsForDisplay, _preSelectedTags, _tags, _preSelectedPersons, _persons,
        _preSelectedPlaces, _places
    ) { state, events, eventsForDisplay, preSelectedTags, tags, preSelectedPersons, persons,
        preSelectedPlaces, places ->
        state.copy(
            events = events,
            eventsForDisplay = eventsForDisplay,
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
        when (action) {
            is ActiveSessionAction.PreSelectedTagClicked -> {
                viewModelScope.launch {
                    val event = Event(
                        sessionId = _sessionId.value, // the value in state can be null
                        elapsedTimeMillis = action.elapsed,
                        color = action.color,
                        tagId = action.tagId,
                        personId = state.value.currentPersonId,
                        placeId = state.value.currentPlaceId,
                    )
                    val id = appDao.upsertEvent(event)
                    _state.update {
                        it.copy(eventForScrollTo = _eventsForDisplay.value.first { it.event.id == id })
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
            }

            is ActiveSessionAction.TrashEventSwiped -> {
                viewModelScope.launch {
                    // we don't want the Event that was retrieved
                    // in the action because it was stale
                    // get the updated one from the DB instead
                    // TODO check the statement above
                    if (action.event4d.event.id != null) {
                        val event = appDao.getEvent(action.event4d.event.id)
                        val trashed = event.copy(inTrash = true)
                        appDao.upsertEvent(trashed)
                    }
                }
            }

            is ActiveSessionAction.EventClicked -> {
                _state.update {
                    it.copy(
                        eventForDialog = action.event,
                        showEventEditionDialog = true
                    )
                }
            }

            is ActiveSessionAction.AcceptEventEditionClicked -> {
                runBlocking {
                    viewModelScope.launch { appDao.upsertEvent(action.event4d.event) }
                }
                _state.update {
                    it.copy(
                        showEventEditionDialog = false,
                        eventForScrollTo = action.event4d
                    )
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

            is ActiveSessionAction.PreSelectedPersonClicked -> {
                val personId = if (action.personId == state.value.currentPersonId) null
                else action.personId
                _state.update { it.copy(currentPersonId = personId) }
            }

            is ActiveSessionAction.PreSelectedPlaceClicked -> {
                val placeId = if (action.placeId == state.value.currentPlaceId) null
                else action.placeId
                _state.update { it.copy(currentPlaceId = placeId) }
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
        val sessionId = savedStateHandle.get<Long>("sessionId") ?: 0
        _sessionId.update { sessionId }
        viewModelScope.launch {
            _state.update {
                it.copy(currentSession = appDao.getSession(sessionId))
            }
            if (state.value.events.isNotEmpty())
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

class SortProvider {

    companion object {

        @OptIn(ExperimentalCoroutinesApi::class)
        fun getTagSort(appDao: AppDao, viewModelScope: CoroutineScope) =
            appDao.getPreferences().mapLatest { prefsList ->
                prefsList.firstOrNull { item ->
                    item.key == PrefKey.TAG_SORT.name
                }?.getLabelSort() ?: LabelSort.COLOR
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SessionSort.LAST_ACCESS)


        @OptIn(ExperimentalCoroutinesApi::class)
        fun getPersonSort(appDao: AppDao, viewModelScope: CoroutineScope) =
            appDao.getPreferences().mapLatest { prefsList ->
                prefsList.firstOrNull { item ->
                    item.key == PrefKey.PERSON_SORT.name
                }?.getLabelSort() ?: LabelSort.COLOR
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SessionSort.LAST_ACCESS)

        @OptIn(ExperimentalCoroutinesApi::class)
        fun getPlaceSort(appDao: AppDao, viewModelScope: CoroutineScope) =
            appDao.getPreferences().mapLatest { prefsList ->
                prefsList.firstOrNull { item ->
                    item.key == PrefKey.PLACE_SORT.name
                }?.getLabelSort() ?: LabelSort.COLOR
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SessionSort.LAST_ACCESS)

        @OptIn(ExperimentalCoroutinesApi::class)
        fun getSessionSort(appDao: AppDao, viewModelScope: CoroutineScope) =
            appDao.getPreferences().mapLatest { prefsList ->
                prefsList.firstOrNull { item ->
                    item.key == PrefKey.SESSION_SORT.name
                }?.getSessionSort() ?: SessionSort.NAME
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SessionSort.LAST_ACCESS)
    }
}
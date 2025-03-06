package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.EventFilterAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.type.LabelSort
import com.deslomator.tagtimer.state.EventFilterState
import com.deslomator.tagtimer.ui.theme.hue
import com.deslomator.tagtimer.util.combine
import com.deslomator.tagtimer.util.toColor
import com.deslomator.tagtimer.util.toCsv
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EventFilterViewModel(
    private val appDao: AppDao,
    seshId: Long?
): ViewModel() {

    private val _sessionId = MutableStateFlow(0L)
    private val _currentTags = MutableStateFlow(emptyList<Label>())
    private val _currentPerson = MutableStateFlow(Label())
    private val _currentPlace = MutableStateFlow(Label())
    // sorting in this screen is independent from global sorting preference
    // so we don't get it from the AppDao
    private val _tagSort = MutableStateFlow(LabelSort.COLOR)
    private val _personSort = MutableStateFlow(LabelSort.NAME)
    private val _placeSort = MutableStateFlow(LabelSort.NAME)
    private val _state = MutableStateFlow(EventFilterState())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _eventsForDisplay = _sessionId
        .flatMapLatest {
            appDao.getEventsForDisplay(_sessionId.value)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _filteredEvents = combine(
        _eventsForDisplay, _currentPerson, _currentPlace, _currentTags
    ) { eventsForDisplay, currentPerson, currentPlace, currentTags ->
        eventsForDisplay
            .filter { event4d ->
                (if (currentPlace.name.isEmpty()) true else event4d.place?.name == currentPlace.name) &&
                        (if (currentPerson.name.isEmpty()) true else event4d.person?.name == currentPerson.name) &&
                        (if (currentTags.isEmpty()) true else currentTags.map{ it.name }.contains(event4d.tag?.name))
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _usedTags = _tagSort.flatMapLatest { sort ->
        if (sort == LabelSort.NAME) appDao.getUsedTags(_sessionId.value)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getUsedTags(_sessionId.value)
            .map { lst -> lst.sortedBy { it.color.toColor().hue() } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _usedPersons = _personSort.flatMapLatest { sort ->
        if (sort == LabelSort.NAME) appDao.getUsedPersons(_sessionId.value)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getUsedPersons(_sessionId.value)
            .map { lst -> lst.sortedBy { it.color.toColor().hue() } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _usedPlaces = _placeSort.flatMapLatest { sort ->
        if (sort == LabelSort.NAME) appDao.getUsedPlaces(_sessionId.value)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getUsedPlaces(_sessionId.value)
            .map { lst -> lst.sortedBy { it.color.toColor().hue() } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _query = combine(
        _currentPerson, _currentPlace, _currentTags
    ) { currentPerson, currentPlace, currentTags ->
        val ts = currentTags.map{ it.name }.toMutableList()
        ts.add(currentPerson.name)
        ts.add(currentPlace.name)
        ts.filter { it.isNotEmpty() }.joinToString(", ")
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val state = combine(
        _state, _filteredEvents, _usedTags, _usedPersons, _usedPlaces, _query, _currentPerson, _currentPlace, _currentTags
    ) { state, filteredEvents, tags, persons, places, query, currentPerson, currentPlace, currentTags ->
        state.copy(
            filteredEvents = filteredEvents,
            tags = tags,
            persons = persons,
            places = places,
            query = query,
            currentPerson = currentPerson,
            currentPlace = currentPlace,
            currentTags = currentTags
        )
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), EventFilterState()
    )

    fun onAction(action: EventFilterAction) {
        when(action) {
            is EventFilterAction.EventClicked -> {
                _state.update { it.copy(
                    eventForDialog = action.event4d,
                    showEventEditionDialog = true
                ) }
            }

            is EventFilterAction.AcceptEventEditionClicked -> {
                viewModelScope.launch { appDao.upsertEvent(action.event) }
                /*
                 state takes some time to update after upserting the event;
                 workaround: we take the updated event out of the list and
                 compare it with the rest of the list to set the new duration
                */
                // TODO understand this
                val maxInList = state.value.filteredEvents
                    .filter { it.event.id != action.event.id }
                    .maxOfOrNull { it.event.elapsedTimeMillis } ?: 0
                val duration = maxOf(maxInList, action.event.elapsedTimeMillis)
                val session = state.value.currentSession.copy(durationMillis = duration)
                _state.update {
                    it.copy(
                        currentSession = session,
                        showEventEditionDialog = false,
//                        eventForScrollTo = action.event
                    )
                }
            }
            EventFilterAction.DismissEventEditionDialog -> {
                _state.update { it.copy(showEventEditionDialog = false) }
            }
            EventFilterAction.EventsExported -> {
                _state.update { it.copy(exportEvents = false) }
            }

            is EventFilterAction.UsedPersonClicked -> {
                val person = if (action.person == state.value.currentPerson) Label()
                else action.person
                _currentPerson.update { person }
            }

            is EventFilterAction.UsedPlaceClicked -> {
                val place = if (action.place == state.value.currentPlace) Label()
                else action.place
                _currentPlace.update { place }
            }

            is EventFilterAction.UsedTagClicked -> {
                val newCurrentTags = state.value.currentTags.toMutableList()
                if (newCurrentTags.map{ it.name }.contains(action.tag.name) ) {
                    newCurrentTags.remove(action.tag)
                } else {
                    newCurrentTags.add(action.tag)
                }
                _currentTags.update { newCurrentTags }
            }

            is EventFilterAction.ExportFilteredEventsClicked -> {
                _state.update { it.copy(
                    dataToExport = action.filteredEvents.toCsv(
                        session = state.value.currentSession,
                        filtered = true
                    ),
                    exportEvents = true
                ) }
            }

            is EventFilterAction.SetPersonSort -> {
                _personSort.update { action.personSort }
            }

            is EventFilterAction.SetPlaceSort -> {
                _placeSort.update { action.placeSort }
            }

            is EventFilterAction.SetTagSort -> {
                _tagSort.update { action.labelSort }
            }
        }
    }

    init {
        val sessionId = seshId ?: 0
        _sessionId.update { sessionId }
        viewModelScope.launch {
            _state.update {
                it.copy(currentSession = appDao.getSession(sessionId))
            }
        }
    }

    companion object {
        private const val TAG = "EventFilterViewModel"
    }
}
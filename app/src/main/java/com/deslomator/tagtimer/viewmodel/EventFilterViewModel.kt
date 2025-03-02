package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.EventFilterAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.type.LabelSort
import com.deslomator.tagtimer.model.type.LabelType
import com.deslomator.tagtimer.state.EventFilterState
import com.deslomator.tagtimer.ui.theme.hue
import com.deslomator.tagtimer.util.combine
import com.deslomator.tagtimer.util.toColor
import com.deslomator.tagtimer.util.toCsv
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventFilterViewModel @Inject constructor(
    private val appDao: AppDao, savedStateHandle: SavedStateHandle
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

    private val _tags = appDao.getActiveLabels(LabelType.TAG.typeId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _persons = appDao.getActiveLabels(LabelType.PERSON.typeId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _places = appDao.getActiveLabels(LabelType.PLACE.typeId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _filteredEvents = combine(
        _eventsForDisplay, _currentPerson, _currentPlace, _currentTags
    ) { eventForDisplay, currentPerson, currentPlace, currentTags ->
        eventForDisplay
            .filter { event4d ->
                (if (currentPlace.name.isEmpty()) true else event4d.getPlaceName() == currentPlace.name) &&
                        (if (currentPerson.name.isEmpty()) true else event4d.getPersonName() == currentPerson.name) &&
                        (if (currentTags.isEmpty()) true else currentTags.map{ it.name }.contains(event4d.getTagName()))
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _usedPersons = combine(
        _persons, _eventsForDisplay, _personSort
    ) { persons, eventForDisplay, personSort ->
        persons
            .filter { person ->
                person.name.isNotEmpty() &&
                        eventForDisplay.map { it.getPersonName() }.contains(person.name)
            }.distinctBy { it.name }
            .sortedWith(
                when (personSort) {
                    LabelSort.COLOR -> compareBy { it.color.toColor().hue() }
                    LabelSort.NAME -> compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
                }
            )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _usedPlaces = combine(
        _places, _eventsForDisplay, _placeSort
    ) { places, eventForDisplay, placeSort ->
        places
            .filter { place ->
                place.name.isNotEmpty() &&
                        eventForDisplay.map { it.getPlaceName() }.contains(place.name)
            }.distinctBy { it.name }
            .sortedWith(
                when (placeSort) {
                    LabelSort.COLOR -> compareBy { it.color.toColor().hue() }
                    LabelSort.NAME -> compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
                }
            )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _usedTags = combine(
        _tags, _eventsForDisplay, _tagSort
    ) { tags, eventForDisplay, tagSort ->
        tags
            .filter { tag ->
                tag.name.isNotEmpty() &&
                        eventForDisplay.map { it.getTagName() }.contains(tag.name)
            }.distinctBy { it.name }
            .sortedWith(
                when (tagSort) {
                    LabelSort.COLOR -> compareBy { it.color.toColor().hue() }
                    LabelSort.NAME -> compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
                }
            )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _query = combine(
        _currentPerson, _currentPlace, _currentTags
    ) { currentPerson, currentPlace, currentTags ->
        val ts = currentTags.toMutableList()
        ts.add(currentPerson)
        ts.add(currentPlace)
        ts.filter { it.name.isNotEmpty() }.joinToString(", ")
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val state = combine(
        _state, _filteredEvents, _usedTags, _usedPersons, _usedPlaces, _query, _currentPerson, _currentPlace, _currentTags
    ) { state, events, tags, persons, places, query, currentPerson, currentPlace, currentTags ->
        state.copy(
            events = events,
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
                viewModelScope.launch { appDao.upsertEvent(action.event4d.event) }
                /*
                 state takes some time to update after upserting the event;
                 workaround: we take the updated event out of the list and
                 compare it with the rest of the list to set the new duration
                */
                // TODO understand this
                val maxInList = state.value.events
                    .filter { it.event.id != action.event4d.event.id }
                    .maxOfOrNull { it.event.elapsedTimeMillis } ?: 0
                val duration = maxOf(maxInList, action.event4d.event.elapsedTimeMillis)
                val session = state.value.currentSession.copy(durationMillis = duration)
                _state.update {
                    it.copy(
                        currentSession = session,
                        showEventEditionDialog = false,
                        eventForScrollTo = action.event4d
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
        val sessionId = savedStateHandle.get<Long>("sessionId") ?: 0
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
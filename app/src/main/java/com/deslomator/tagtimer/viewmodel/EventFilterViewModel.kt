package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.EventFilterAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.type.LabelSort
import com.deslomator.tagtimer.state.EventFilterState
import com.deslomator.tagtimer.ui.theme.hue
import com.deslomator.tagtimer.util.combine
import com.deslomator.tagtimer.util.toCsv
import com.deslomator.tagtimer.util.toColor
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
    private val _currentTags = MutableStateFlow(emptyList<String>())
    private val _currentPerson = MutableStateFlow("")
    private val _currentPlace = MutableStateFlow("")
    // sorting in this screen is independent from global sorting preference
    // so we don't get it from the AppDao
    private val _tagSort = MutableStateFlow(LabelSort.COLOR)
    private val _personSort = MutableStateFlow(LabelSort.NAME)
    private val _placeSort = MutableStateFlow(LabelSort.NAME)
    private val _state = MutableStateFlow(EventFilterState())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _events = _sessionId
        .flatMapLatest {
            appDao.getActiveEventsForSession(_sessionId.value)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _tags = appDao.getActiveTags()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _persons = appDao.getActivePersons()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _places = appDao.getActivePlaces()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _filteredEvents = combine(
        _events, _currentPerson, _currentPlace, _currentTags
    ) { events, person, place, tags ->
        events
            .filter { event ->
                (if (place.isEmpty()) true else event.place == place) &&
                        (if (person.isEmpty()) true else event.person == person) &&
                        (if (tags.isEmpty()) true else tags.contains(event.tag))
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _usedPersons = combine(
        _persons, _events, _personSort
    ) { persons, events, personSort ->
        persons
            .filter { person ->
                person.name.isNotEmpty() &&
                        events.map { it.person }.contains(person.name)
            }.distinctBy { it.name }
            .sortedWith(
                when (personSort) {
                    LabelSort.COLOR -> compareBy { it.color.toColor().hue() }
                    LabelSort.NAME -> compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
                }
            )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _usedPlaces = combine(
        _places, _events, _placeSort
    ) { places, events, placeSort ->
        places
            .filter { place ->
                place.name.isNotEmpty() &&
                        events.map { it.place }.contains(place.name)
            }.distinctBy { it.name }
            .sortedWith(
                when (placeSort) {
                    LabelSort.COLOR -> compareBy { it.color.toColor().hue() }
                    LabelSort.NAME -> compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
                }
            )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _usedTags = combine(
        _tags, _events, _tagSort
    ) { tags, events, tagSort ->
        tags
            .filter { tag ->
                tag.name.isNotEmpty() &&
                        events.map { it.tag }.contains(tag.name)
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
    ) { person, place, tags ->
        val ts = tags.toMutableList()
        ts.add(person)
        ts.add(place)
        ts.filter { it.isNotEmpty() }.joinToString(", ")
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
                createNewLabels(action.event)
            }
            EventFilterAction.DismissEventEditionDialog -> {
                _state.update { it.copy(showEventEditionDialog = false) }
            }
            EventFilterAction.EventsExported -> {
                _state.update { it.copy(exportEvents = false) }
            }
            is EventFilterAction.UsedPersonClicked -> {
                val person = if (action.personId == state.value.currentPerson) ""
                else action.personId
                _currentPerson.update { person }
            }
            is EventFilterAction.UsedPlaceClicked -> {
                val place = if (action.placeId == state.value.currentPlace) ""
                else action.placeId
                _currentPlace.update { place }
            }
            is EventFilterAction.UsedTagClicked -> {
                val newCurrentTags = state.value.currentTags.toMutableList()
                if (newCurrentTags.contains(action.tag.name) ) {
                    newCurrentTags.remove(action.tag.name)
                } else {
                    newCurrentTags.add(action.tag.name)
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

    /**
    create new Labels if an Event edition resulted in non existent label names
     */
    private fun createNewLabels(action: Event) {
        if (!_persons.value.map { it.name }.contains(action.person))
            viewModelScope.launch {
                appDao.upsertPerson(
                    Label.Person(
                        name = action.person,
                        color = action.color
                    )
                )
            }
        if (!_places.value.map { it.name }.contains(action.place))
            viewModelScope.launch {
                appDao.upsertPlace(
                    Label.Place(
                        name = action.place,
                        color = action.color
                    )
                )
            }
        if (!_tags.value.map { it.name }.contains(action.tag))
            viewModelScope.launch {
                appDao.upsertTag(
                    Label.Tag(
                        name = action.tag,
                        color = action.color
                    )
                )
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
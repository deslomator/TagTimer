package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.EventFilterAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.ExportedEvent
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.type.Sort
import com.deslomator.tagtimer.state.EventFilterState
import com.deslomator.tagtimer.ui.theme.hue
import com.deslomator.tagtimer.util.combine
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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class EventFilterViewModel @Inject constructor(
    private val appDao: AppDao, savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _sessionId = MutableStateFlow(0L)
    private val _currentTag = MutableStateFlow("")
    private val _currentPerson = MutableStateFlow("")
    private val _currentPlace = MutableStateFlow("")
    private val _tagSort = MutableStateFlow(Sort.COLOR)
    private val _personSort = MutableStateFlow(Sort.NAME)
    private val _placeSort = MutableStateFlow(Sort.NAME)
    private val _state = MutableStateFlow(EventFilterState())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _events = _sessionId
        .flatMapLatest {
            appDao.getActiveEventsForSession(_sessionId.value)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _tags = appDao.getActiveTags()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _persons = appDao.getActivePersons()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _places = appDao.getActivePlaces()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _filteredEvents = combine(
        _events, _currentPerson, _currentPlace, _currentTag
    ) { events, person, place, tag ->
        events
            .filter { event ->
                (if (place.isEmpty()) true else event.place == place) &&
                        (if (person.isEmpty()) true else event.person == person) &&
                        (if (tag.isEmpty()) true else event.tag == tag)
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

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
                    Sort.COLOR -> compareBy { it.color.toColor().hue() }
                    Sort.NAME -> compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
                }
            )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

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
                    Sort.COLOR -> compareBy { it.color.toColor().hue() }
                    Sort.NAME -> compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
                }
            )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

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
                    Sort.COLOR -> compareBy { it.color.toColor().hue() }
                    Sort.NAME -> compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
                }
            )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _query = combine(
        _currentPerson, _currentPlace, _currentTag
    ) { person, place, tag ->
        listOf(person, place, tag).filter { it.isNotEmpty() }.joinToString(", ")
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")

    val state = combine(
        _state, _filteredEvents, _usedTags, _usedPersons, _usedPlaces, _query, _currentPerson, _currentPlace, _currentTag
    ) { state, events, tags, persons, places, query, currentPerson, currentPlace, currentTag ->
        state.copy(
            events = events,
            tags = tags,
            persons = persons,
            places = places,
            query = query,
            currentPerson = currentPerson,
            currentPlace = currentPlace,
            currentTag = currentTag
        )
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), EventFilterState()
    )

    fun onAction(action: EventFilterAction) {
        when(action) {
            is EventFilterAction.EventClicked -> {
                _state.update { it.copy(
                    eventForDialog = action.event,
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
                val person = if (action.personName == state.value.currentPerson) ""
                else action.personName
                _currentPerson.update { person }
            }
            is EventFilterAction.UsedPlaceClicked -> {
                val place = if (action.placeName == state.value.currentPlace) ""
                else action.placeName
                _currentPlace.update { place }
            }
            is EventFilterAction.UsedTagClicked -> {
                val tag = if (action.tag.name == state.value.currentTag) ""
                else action.tag.name
                _currentTag.update { tag }
            }
            is EventFilterAction.ExportFilteredEventsClicked -> {
                setEventsToExport(action.filteredEvents)
            }
            is EventFilterAction.SetPersonSort -> {
                _personSort.update { action.personSort }
            }
            is EventFilterAction.SetPlaceSort -> {
                _placeSort.update { action.placeSort }
            }
            is EventFilterAction.SetTagSort -> {
                _tagSort.update { action.tagSort }
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

    private fun setEventsToExport(filteredEvents: List<Event> = emptyList()) {
        val json = Json.encodeToString( filteredEvents.map { ExportedEvent(it) })
        _state.update {
            it.copy(
                dataToExport = json,
                exportEvents = true
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
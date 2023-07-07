package com.deslomator.tagtimer.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.EventFilterAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.ExportedEvent
import com.deslomator.tagtimer.model.type.Sort
import com.deslomator.tagtimer.state.EventFilterState
import com.deslomator.tagtimer.ui.theme.hue
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
class EventFilterViewModel @Inject constructor(
    private val appDao: AppDao, savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _sessionId = MutableStateFlow(0)
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
                    Sort.COLOR -> compareBy { Color(it.color).hue() }
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
                    Sort.COLOR -> compareBy { Color(it.color).hue() }
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
                    Sort.COLOR -> compareBy { Color(it.color).hue() }
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
            }
            EventFilterAction.DismissEventEditionDialog -> {
                _state.update { it.copy(showEventEditionDialog = false) }
            }
            EventFilterAction.EventsExported -> {
                _state.update { it.copy(exportEvents = false) }
            }
            is EventFilterAction.usedPersonClicked -> {
                val person = if (action.personName == state.value.currentPerson) ""
                else action.personName
                _currentPerson.update { person }
            }
            is EventFilterAction.usedPlaceClicked -> {
                val place = if (action.placeName == state.value.currentPlace) ""
                else action.placeName
                _currentPlace.update { place }
            }
            is EventFilterAction.UsedTagClicked -> {
                val tag = if (action.tagName == state.value.currentTag) ""
                else action.tagName
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

    private fun setEventsToExport(filteredEvents: List<Event> = emptyList()) {
        val json = Json.encodeToString( filteredEvents.map { ExportedEvent(it) })
        _state.update {
            it.copy(
                dataToExport = json,
                exportEvents = true
            )
        }
    }

    private inline fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> combine(
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

    init {
        val sessionId = savedStateHandle.get<Int>("sessionId") ?: 0
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
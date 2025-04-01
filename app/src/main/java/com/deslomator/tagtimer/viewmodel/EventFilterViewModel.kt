package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.EventFilterAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.ancillary.PreferenceProvider
import com.deslomator.tagtimer.model.type.LabelSort
import com.deslomator.tagtimer.model.type.LabelType
import com.deslomator.tagtimer.state.EventFilterState
import com.deslomator.tagtimer.ui.theme.hue
import com.deslomator.tagtimer.util.combine
import com.deslomator.tagtimer.util.toColor
import com.deslomator.tagtimer.util.toCsv
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EventFilterViewModel(
    private val appDao: AppDao,
    private val sessionId: Long
): ViewModel() {

    private val _currentTags = MutableStateFlow(emptyList<Label>())
    private val _currentPerson = MutableStateFlow(Label())
    private val _currentPlace = MutableStateFlow(Label())

    private val _state = MutableStateFlow(EventFilterState())

    private val _prefs = appDao.getPreferences()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _prefProvider = _prefs.mapLatest { prefs ->
        PreferenceProvider(prefs)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), PreferenceProvider())

    private val _eventsForDisplay = appDao.getEventsForDisplay(sessionId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _filteredEvents = combine(
        _eventsForDisplay, _currentPerson, _currentPlace, _currentTags
    ) { eventsForDisplay, currentPerson, currentPlace, currentTags ->
        eventsForDisplay
            .filter { event4d ->
                (if (currentPlace.name.isEmpty()) true else event4d.place?.id === currentPlace.id) &&
                        (if (currentPerson.name.isEmpty()) true else event4d.person?.id == currentPerson.id) &&
                        (if (currentTags.isEmpty()) true else event4d.tag in currentTags)
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _usedTags = _prefProvider.flatMapLatest { prefProvider ->
        if (prefProvider.tagSort() == LabelSort.NAME) appDao.getUsedTags(sessionId)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getUsedTags(sessionId)
            .map { lst -> lst.sortedBy { it.color.toColor().hue() } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _usedPersons = _prefProvider.flatMapLatest { prefProvider ->
        if (prefProvider.personSort() == LabelSort.NAME) appDao.getUsedPersons(sessionId)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getUsedPersons(sessionId)
            .map { lst -> lst.sortedBy { it.color.toColor().hue() } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _usedPlaces = _prefProvider.flatMapLatest { prefProvider ->
        if (prefProvider.placeSort() == LabelSort.NAME) appDao.getUsedPlaces(sessionId)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getUsedPlaces(sessionId)
            .map { lst -> lst.sortedBy { it.color.toColor().hue() } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _query = combine(
        _currentPerson, _currentPlace, _currentTags
    ) { currentPerson, currentPlace, currentTags ->
        val ts = currentTags.map{ it.name }.toMutableList()
        ts.add(currentPerson.name)
        ts.add(currentPlace.name)
        ts.filter { it.isNotEmpty() }.joinToString(", ")
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")

    @OptIn(ExperimentalCoroutinesApi::class)
    private  val _activeTags = _prefProvider.mapLatest { prefProvider ->
        if (prefProvider.tagSort() == LabelSort.NAME) appDao.getAllActiveTagsList()
            .filter { it.type == LabelType.TAG }.sortedBy { it.name }
        else appDao.getAllActiveTagsList()
            .filter { it.type == LabelType.TAG }.sortedBy { it.color.toColor().hue() }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private  val _activePersons = _prefProvider.mapLatest { prefProvider ->
        if (prefProvider.personSort() == LabelSort.NAME) appDao.getAllActivePersonsList()
            .filter { it.type == LabelType.PERSON }.sortedBy { it.name }
        else appDao.getAllActivePersonsList()
            .filter { it.type == LabelType.PERSON }.sortedBy { it.color.toColor().hue() }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private  val _activePlaces = _prefProvider.mapLatest { prefProvider ->
        if (prefProvider.placeSort() == LabelSort.NAME) appDao.getAllActivePlacesList()
            .filter { it.type == LabelType.PLACE }.sortedBy { it.name }
        else appDao.getAllActivePlacesList()
            .filter { it.type == LabelType.PLACE }.sortedBy { it.color.toColor().hue() }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = combine(
        _state, _filteredEvents, _usedTags, _usedPersons, _usedPlaces, _query, _currentPerson,
        _currentPlace, _currentTags, _activeTags, _activePersons, _activePlaces
    ) { state, filteredEvents, tags, persons, places, query, currentPerson, currentPlace,
        currentTags, activeTags, activePersons, activePlaces ->
        state.copy(
            filteredEvents = filteredEvents,
            tags = tags,
            persons = persons,
            places = places,
            query = query,
            currentPerson = currentPerson,
            currentPlace = currentPlace,
            currentTags = currentTags,
            activeTags = activeTags,
            activePersons = activePersons,
            activePlaces = activePlaces,
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
                val maxInList = state.value.filteredEvents
                    .filter { it.event.id != action.event4d.event.id }
                    .maxOfOrNull { it.event.elapsedTimeMillis } ?: 0
                val duration = maxOf(maxInList, action.event4d.event.elapsedTimeMillis)
                val session = state.value.currentSession.copy(durationMillis = duration)
                viewModelScope.launch(Dispatchers.IO) {
                    appDao.upsertEvent(action.event4d.event)
                    appDao.upsertSession(session)
                }
                _state.update {
                    it.copy(
                        currentSession = session,
                        showEventEditionDialog = false,
                        eventForScrollTo = action.event4d
                    )
                }
            }

            is EventFilterAction.DismissEventEditionDialog -> {
                _state.update { it.copy(showEventEditionDialog = false) }
            }

            is EventFilterAction.EventsExported -> {
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
                if (action.tag in newCurrentTags) {
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
        }
    }

    init {

        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(currentSession = appDao.getSession(sessionId))
            }
        }
    }

    companion object {
        private const val TAG = "EventFilterViewModel"
    }
}
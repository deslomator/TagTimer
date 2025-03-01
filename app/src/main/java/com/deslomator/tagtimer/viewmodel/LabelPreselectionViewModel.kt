package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.LabelPreselectionAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Preselected
import com.deslomator.tagtimer.model.type.LabelSort
import com.deslomator.tagtimer.state.LabelPreselectionState
import com.deslomator.tagtimer.ui.theme.hue
import com.deslomator.tagtimer.util.combine
import com.deslomator.tagtimer.util.toColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LabelPreselectionViewModel @Inject constructor(
    private val appDao: AppDao, savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _sessionId = MutableStateFlow(0L)
    private val _state = MutableStateFlow(LabelPreselectionState())

    private val _tagSort = SortProvider.getTagSort(appDao, viewModelScope)
    private val _personSort = SortProvider.getPersonSort(appDao, viewModelScope)
    private val _placeSort = SortProvider.getPlaceSort(appDao, viewModelScope)

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
        _state, _preSelectedTags, _tags, _preSelectedPersons, _persons,
        _preSelectedPlaces, _places
    ) { state, preSelectedTags, tags, preSelectedPersons, persons,
        preSelectedPlaces, places ->
        state.copy(
            preSelectedTags = preSelectedTags,
            tags = tags,
            preSelectedPersons = preSelectedPersons,
            persons = persons,
            preSelectedPlaces = preSelectedPlaces,
            places = places,
        )
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), LabelPreselectionState()
    )

    fun onAction(action: LabelPreselectionAction) {
        when(action) {
            /*
            TAG
             */
            is LabelPreselectionAction.SelectTagCheckedChange -> {
                viewModelScope.launch {
                    val pst = Preselected(
                        sessionId = _sessionId.value,
                        labelId = action.tag.id!!
                    )
                    delay(UPSERT_DELAY_MS) // TODO what's this delay
                    if (action.checked) {
                        appDao.upsertPreSelectedLabel(pst)
                    } else {
                        appDao.deletePreSelectedLabel(pst)
                    }
                }
            }
            is LabelPreselectionAction.AddNewTagClicked -> {
                _state.update { it.copy(
                    currentTag = Label(),
                    showTagDialog = true,
                    isAddingNewTag = true
                ) }
            }
            is LabelPreselectionAction.EditTagClicked -> {
                _state.update { it.copy(
                    currentTag = action.tag,
                    showTagDialog = true,
                    isEditingTag = true,
                ) }
            }
            is LabelPreselectionAction.AcceptTagEditionClicked -> {
                _state.update { it.copy(
                    showTagDialog = false,
                    isEditingTag = false,
                ) }
                val edited = Label(
                    name = action.label.name,
                    color = action.label.color,
                    type = action.label.type,
                    id = action.label.id
                )
                viewModelScope.launch {
                    appDao.upsertLabel(edited)
                }
            }
            is LabelPreselectionAction.DismissTagDialog -> {
                _state.update { it.copy(
                    showTagDialog = false,
                    isEditingTag = false,
                    isAddingNewTag = false,
                ) }
            }
            is LabelPreselectionAction.DeleteTagClicked -> { // TODO used tag deletion loop
                _state.update { it.copy(showTagDialog = false) }
                viewModelScope.launch {
                    val trashed = action.tag.copy(inTrash = true)
                    appDao.upsertLabel(trashed)
                }
            }
            /*
            PERSON
             */
            is LabelPreselectionAction.SelectPersonCheckedChange -> {
                viewModelScope.launch {
                    val pst = Preselected(
                        sessionId = _sessionId.value,
                        labelId = action.person.id!!
                    )
                    delay(UPSERT_DELAY_MS) // TODO what's this delay
                    if (action.checked) {
                        appDao.upsertPreSelectedLabel(pst)
                    } else {
                        appDao.deletePreSelectedLabel(pst)
                    }
                }
            }
            is LabelPreselectionAction.AddNewPersonClicked -> {
                _state.update { it.copy(
                    currentPerson = Label(),
                    showPersonDialog = true,
                    isAddingNewPerson = true
                ) }
            }
            is LabelPreselectionAction.EditPersonClicked -> {
                _state.update { it.copy(
                    currentPerson = action.person,
                    showPersonDialog = true,
                    isEditingPerson = true,
                ) }
            }
            is LabelPreselectionAction.AcceptPersonEditionClicked -> {
                _state.update { it.copy(
                    showPersonDialog = false,
                    isEditingPerson = false,
                ) }
                val edited = Label(
                    name = action.label.name,
                    color = action.label.color,
                    type = action.label.type,
                    id = action.label.id
                )
                viewModelScope.launch {
                    appDao.upsertLabel(edited)
                }
            }
            is LabelPreselectionAction.DismissPersonDialog -> {
                _state.update { it.copy(
                    showPersonDialog = false,
                    isEditingPerson = false,
                    isAddingNewPerson = false,
                ) }
            }
            is LabelPreselectionAction.DeletePersonClicked -> { // TODO used tag deletion loop
                _state.update { it.copy(showPersonDialog = false) }
                viewModelScope.launch {
                    val trashed = action.person.copy(inTrash = true)
                    appDao.upsertLabel(trashed)
                }
            }
            /*
            PLACE
             */
            is LabelPreselectionAction.SelectPlaceCheckedChange -> {
                viewModelScope.launch {
                    val pst = Preselected(
                        sessionId = _sessionId.value,
                        labelId = action.place.id!!
                    )
                    delay(UPSERT_DELAY_MS) // TODO what's this delay
                    if (action.checked) {
                        appDao.upsertPreSelectedLabel(pst)
                    } else {
                        appDao.deletePreSelectedLabel(pst)
                    }
                }
            }
            is LabelPreselectionAction.AddNewPlaceClicked -> {
                _state.update { it.copy(
                    currentPlace = Label(),
                    showPlaceDialog = true,
                    isAddingNewPlace = true
                ) }
            }
            is LabelPreselectionAction.EditPlaceClicked -> {
                _state.update { it.copy(
                    currentPlace = action.place,
                    showPlaceDialog = true,
                    isEditingPlace = true,
                ) }
            }
            is LabelPreselectionAction.AcceptPlaceEditionClicked -> {
                _state.update { it.copy(
                    showPlaceDialog = false,
                    isEditingPlace = false,
                ) }
                val edited = Label(
                    name = action.label.name,
                    color = action.label.color,
                    type = action.label.type,
                    id = action.label.id
                )
                viewModelScope.launch {
                    appDao.upsertLabel(edited)
                }
            }
            is LabelPreselectionAction.DismissPlaceDialog -> {
                _state.update { it.copy(
                    showPlaceDialog = false,
                    isEditingPlace = false,
                    isAddingNewPlace = false,
                ) }
            }
            is LabelPreselectionAction.DeletePlaceClicked -> { // TODO used tag deletion loop
                _state.update { it.copy(showPlaceDialog = false) }
                viewModelScope.launch {
                    val trashed = action.place.copy(inTrash = true)
                    appDao.upsertLabel(trashed)
                }
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
        private const val UPSERT_DELAY_MS = 300L
        private const val TAG = "LabelPreselectionViewModel"
    }
}
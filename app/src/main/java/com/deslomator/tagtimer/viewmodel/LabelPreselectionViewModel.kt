package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.LabelPreselectionAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Preselected
import com.deslomator.tagtimer.state.LabelPreselectionState
import com.deslomator.tagtimer.util.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
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

    private val _tags = appDao.getActiveTags()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _preSelectedTags = _sessionId
        .flatMapLatest {
            appDao.getPreSelectedTagsForSession(_sessionId.value)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _persons = appDao.getActivePersons()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _preSelectedPersons = _sessionId
        .flatMapLatest {
            appDao.getPreSelectedPersonsForSession(_sessionId.value)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _places = appDao.getActivePLaces()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _preSelectedPlaces = _sessionId
        .flatMapLatest {
            appDao.getPreSelectedPlacesForSession(_sessionId.value)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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
                    val pst = Preselected.Tag(
                        sessionId = _sessionId.value,
                        labelId = action.tag.id
                    )
                    delay(UPSERT_DELAY_MS)
                    if (action.checked) {
                        appDao.upsertPreSelectedTag(pst)
                    } else {
                        appDao.deletePreSelectedTag(pst)
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
                val edited = Label.Tag(
                    name = action.name,
                    color = action.color
                )
                viewModelScope.launch {
                    appDao.deleteTag(state.value.currentTag)
                    appDao.upsertTag(edited)
                }
                viewModelScope.launch { // restore [or remove orphaned] preselection state
                    val psToDelete = Preselected.Tag(
                        sessionId = _sessionId.value,
                        labelId = state.value.currentTag.id
                    )
                    if (state.value.preSelectedTags.contains(psToDelete)) {
                        appDao.deletePreSelectedTag(psToDelete)
                        val psToInsert = Preselected.Tag(
                            sessionId = _sessionId.value,
                            labelId = edited.id
                        )
                        appDao.upsertPreSelectedTag(psToInsert)
                    }
                }
            }
            is LabelPreselectionAction.DismissTagDialog -> {
                _state.update { it.copy(
                    showTagDialog = false,
                    isEditingTag = false,
                    isAddingNewTag = false,
                ) }
            }
            is LabelPreselectionAction.DeleteTagClicked -> {
                _state.update { it.copy(showTagDialog = false) }
                viewModelScope.launch {
                    val trashed = action.tag.copy(inTrash = true)
                    appDao.upsertTag(trashed)
                    val orphanedPreselectedTag = Preselected.Tag(
                        sessionId = _sessionId.value,
                        labelId = action.tag.id
                    )
                    appDao.deletePreSelectedTag(orphanedPreselectedTag)
                }
            }
            /*
            PERSON
             */
            is LabelPreselectionAction.SelectPersonCheckedChange -> {
                viewModelScope.launch {
                    val psPr = Preselected.Person(
                        sessionId = state.value.currentSession.id!!,
                        labelId = action.person.id
                    )
                    delay(UPSERT_DELAY_MS)
                    if (action.checked) {
                        appDao.upsertPreSelectedPerson(psPr)
                    } else {
                        appDao.deletePreSelectedPerson(psPr)
                    }
                }
            }
            is LabelPreselectionAction.AddNewPersonClicked -> {
                _state.update { it.copy(
                    currentPerson = Label.Person(),
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
                val edited = Label.Person(
                    name = action.name,
                    color = action.color
                )
                viewModelScope.launch {
                    appDao.deletePerson(state.value.currentPerson)
                    appDao.upsertPerson(edited)
                }
                viewModelScope.launch { // restore [or remove orphaned] preselection state
                    val psToDelete = Preselected.Person(
                        sessionId = _sessionId.value,
                        labelId = state.value.currentPerson.id
                    )
                    if (state.value.preSelectedPersons.contains(psToDelete)) {
                        appDao.deletePreSelectedPerson(psToDelete)
                        val psToInsert = Preselected.Person(
                            sessionId = _sessionId.value,
                            labelId = edited.id
                        )
                        appDao.upsertPreSelectedPerson(psToInsert)
                    }
                }
            }
            is LabelPreselectionAction.DismissPersonDialog -> {
                _state.update { it.copy(
                    showPersonDialog = false,
                    isEditingPerson = false,
                    isAddingNewPerson = false,
                ) }
            }
            is LabelPreselectionAction.DeletePersonClicked -> {
                _state.update { it.copy(showPersonDialog = false) }
                viewModelScope.launch {
                    val trashed = action.person.copy(inTrash = true)
                    appDao.upsertPerson(trashed)
                    val orphanedPreselectedPerson = Preselected.Person(
                        sessionId = _sessionId.value,
                        labelId = action.person.id
                    )
                    appDao.deletePreSelectedPerson(orphanedPreselectedPerson)
                }
            }
            /*
            PLACE
             */
            is LabelPreselectionAction.SelectPlaceCheckedChange -> {
                viewModelScope.launch {
                    val psPl = Preselected.Place(
                        sessionId = state.value.currentSession.id!!,
                        labelId = action.place.id
                    )
                    delay(UPSERT_DELAY_MS)
                    if (action.checked) {
                        appDao.upsertPreSelectedPlace(psPl)
                    } else {
                        appDao.deletePreSelectedPlace(psPl)
                    }
                }
            }
            is LabelPreselectionAction.AddNewPlaceClicked -> {
                _state.update { it.copy(
                    currentPlace = Label.Place(),
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
                val edited = Label.Place(
                    name = action.name,
                    color = action.color
                )
                viewModelScope.launch {
                    appDao.deletePlace(state.value.currentPlace)
                    appDao.upsertPlace(edited)
                }
                viewModelScope.launch { // restore [or remove orphaned] preselection state
                    val psToDelete = Preselected.Place(
                        sessionId = _sessionId.value,
                        labelId = state.value.currentPlace.id
                    )
                    if (state.value.preSelectedPlaces.contains(psToDelete)) {
                        appDao.deletePreSelectedPlace(psToDelete)
                        val psToInsert = Preselected.Place(
                            sessionId = _sessionId.value,
                            labelId = edited.id
                        )
                        appDao.upsertPreSelectedPlace(psToInsert)
                    }
                }
            }
            is LabelPreselectionAction.DismissPlaceDialog -> {
                _state.update { it.copy(
                    showPlaceDialog = false,
                    isEditingPlace = false,
                    isAddingNewPlace = false,
                ) }
            }
            is LabelPreselectionAction.DeletePlaceClicked -> {
                _state.update { it.copy(showPlaceDialog = false) }
                viewModelScope.launch {
                    val trashed = action.place.copy(inTrash = true)
                    appDao.upsertPlace(trashed)
                    val orphanedPreselectedPlace = Preselected.Place(
                        sessionId = _sessionId.value,
                        labelId = action.place.id
                    )
                    appDao.deletePreSelectedPlace(orphanedPreselectedPlace)
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
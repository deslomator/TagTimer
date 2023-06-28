package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.LabelsTabAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Person
import com.deslomator.tagtimer.model.Place
import com.deslomator.tagtimer.model.Tag
import com.deslomator.tagtimer.state.LabelsTabState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LabelsTabViewModel @Inject constructor(
    private val appDao: AppDao,
): ViewModel() {

    private val _state = MutableStateFlow(LabelsTabState())
    private val _tags = appDao.getActiveTags()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _persons = appDao.getActivePersons()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _places = appDao.getActivePlaces()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val state = combine(_state, _tags, _persons, _places) { state, tags, persons, places ->
        state.copy(
            tags = tags,
            persons = persons,
            places = places,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LabelsTabState())

    fun onAction(action: LabelsTabAction) {
        when(action) {
            is LabelsTabAction.LabelTypeSelected -> {
                _state.update { it.copy(currentLabel = action.type) }
            }
            is LabelsTabAction.AddNewTagClicked -> {
                _state.update { it.copy(
                    showTagDialog = true,
                    isAddingNewTag = true
                ) }
            }
            is LabelsTabAction.AcceptAddNewTagClicked -> {
                _state.update { it.copy(
                    showTagDialog = false,
                    isAddingNewTag = false,
                ) }
                viewModelScope.launch { appDao.upsertTag(action.tag) }
            }
            is LabelsTabAction.EditTagClicked -> {
                _state.update { it.copy(
                    showTagDialog = true,
                    isEditingTag = true,
                    currentTag = action.tag,
                ) }
            }
            is LabelsTabAction.AcceptTagEditionClicked -> {
                _state.update { it.copy(
                    showTagDialog = false,
                    isEditingTag = false,
                    currentTag = Tag()
                ) }
                viewModelScope.launch { appDao.upsertTag(action.tag) }
            }
            is LabelsTabAction.DismissTagDialog -> {
                _state.update { it.copy(
                    showTagDialog = false,
                    isEditingTag = false,
                    isAddingNewTag = false,
                ) }
            }
            is LabelsTabAction.TrashTagSwiped -> {
                viewModelScope.launch {
                    val trashed = action.tag.copy(inTrash = true)
                    appDao.upsertTag(trashed)
//                    Log.d(TAG, "TagsScreenAction.TrashTagSwiped $trashed")
                }
            }
            /*
            PERSON
             */
            is LabelsTabAction.AddNewPersonClicked -> {
                _state.update { it.copy(
                    showPersonDialog = true,
                    isAddingNewPerson = true
                ) }
            }
            is LabelsTabAction.AcceptAddNewPersonClicked -> {
                _state.update { it.copy(
                    showPersonDialog = false,
                    isAddingNewPerson = false,
                ) }
                viewModelScope.launch { appDao.upsertPerson(action.person) }
            }
            is LabelsTabAction.EditPersonClicked -> {
                _state.update { it.copy(
                    showPersonDialog = true,
                    isEditingPerson = true,
                    currentPerson = action.person,
                ) }
            }
            is LabelsTabAction.AcceptPersonEditionClicked -> {
                _state.update { it.copy(
                    showPersonDialog = false,
                    isEditingPerson = false,
                    currentPerson = Person()
                ) }
                viewModelScope.launch { appDao.upsertPerson(action.person) }
            }
            is LabelsTabAction.DismissPersonDialog -> {
                _state.update { it.copy(
                    showPersonDialog = false,
                    isEditingPerson = false,
                    isAddingNewPerson = false,
                ) }
            }
            is LabelsTabAction.TrashPersonSwiped -> {
                viewModelScope.launch {
                    val trashed = action.person.copy(inTrash = true)
                    appDao.upsertPerson(trashed)
                }
            }
            /*
            PLACE
             */
            is LabelsTabAction.AddNewPlaceClicked -> {
                _state.update { it.copy(
                    showPlaceDialog = true,
                    isAddingNewPlace = true
                ) }
            }
            is LabelsTabAction.AcceptAddNewPlaceClicked -> {
                _state.update { it.copy(
                    showPlaceDialog = false,
                    isAddingNewPlace = false,
                ) }
                viewModelScope.launch { appDao.upsertPlace(action.place) }
            }
            is LabelsTabAction.EditPlaceClicked -> {
                _state.update { it.copy(
                    showPlaceDialog = true,
                    isEditingPlace = true,
                    currentPlace = action.place,
                ) }
            }
            is LabelsTabAction.AcceptPlaceEditionClicked -> {
                _state.update { it.copy(
                    showPlaceDialog = false,
                    isEditingPlace = false,
                    currentPlace = Place()
                ) }
                viewModelScope.launch { appDao.upsertPlace(action.place) }
            }
            is LabelsTabAction.DismissPlaceDialog -> {
                _state.update { it.copy(
                    showPlaceDialog = false,
                    isEditingPlace = false,
                    isAddingNewPlace = false,
                ) }
            }
            is LabelsTabAction.TrashPlaceSwiped -> {
                viewModelScope.launch {
                    val trashed = action.place.copy(inTrash = true)
                    appDao.upsertPlace(trashed)
                }
            }
        }
    }

    companion object {
        private const val TAG = "TagsScreenViewModel"
    }
}
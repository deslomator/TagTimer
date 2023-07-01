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
            is LabelsTabAction.AddNewTagClicked -> {
                _state.update { it.copy(
                    currentTag = Tag(),
                    showTagDialog = true,
                    isAddingNewTag = true
                ) }
            }
            is LabelsTabAction.EditTagClicked -> {
                _state.update { it.copy(
                    currentTag = action.tag,
                    showTagDialog = true,
                    isEditingTag = true,
                ) }
            }
            is LabelsTabAction.AcceptTagEditionClicked -> {
                _state.update { it.copy(
                    showTagDialog = false,
                    isEditingTag = false,
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
                _state.update { it.copy(showTagDialog = false) }
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
                    currentPerson = Person(),
                    showPersonDialog = true,
                    isAddingNewPerson = true
                ) }
            }
            is LabelsTabAction.EditPersonClicked -> {
                _state.update { it.copy(
                    currentPerson = action.person,
                    showPersonDialog = true,
                    isEditingPerson = true,
                ) }
            }
            is LabelsTabAction.AcceptPersonEditionClicked -> {
                _state.update { it.copy(
                    showPersonDialog = false,
                    isEditingPerson = false,
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
                _state.update { it.copy(showPersonDialog = false) }
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
                    currentPlace = Place(),
                    showPlaceDialog = true,
                    isAddingNewPlace = true
                ) }
            }
            is LabelsTabAction.EditPlaceClicked -> {
                _state.update { it.copy(
                    currentPlace = action.place,
                    showPlaceDialog = true,
                    isEditingPlace = true,
                ) }
            }
            is LabelsTabAction.AcceptPlaceEditionClicked -> {
                _state.update { it.copy(
                    showPlaceDialog = false,
                    isEditingPlace = false,
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
                _state.update { it.copy(showPlaceDialog = false) }
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
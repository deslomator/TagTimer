package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.LabelsTabAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Preference
import com.deslomator.tagtimer.model.type.PrefKey
import com.deslomator.tagtimer.model.type.LabelSort
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
    private val _prefs = appDao.getPreferences()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    private val _tags = appDao.getActiveTags()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    private val _persons = appDao.getActivePersons()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    private val _places = appDao.getActivePlaces()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val state = combine(
        _state, _tags, _persons, _places, _prefs
    ) { state, tags, persons, places, prefs ->
        state.copy(
            tags = tags,
            persons = persons,
            places = places,
            tagSort = prefs.firstOrNull { it.key == PrefKey.TAG_SORT.name }?.getLabelSort() ?: LabelSort.COLOR,
            personSort = prefs.firstOrNull { it.key == PrefKey.PERSON_SORT.name }?.getLabelSort() ?: LabelSort.NAME,
            placeSort = prefs.firstOrNull { it.key == PrefKey.PLACE_SORT.name }?.getLabelSort() ?: LabelSort.NAME,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LabelsTabState())

    fun onAction(action: LabelsTabAction) {
        when(action) {
            /*
            TAG
             */
            is LabelsTabAction.AddNewTagClicked -> {
                _state.update { it.copy(
                    currentTag = Label.Tag(),
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
                viewModelScope.launch {
                    appDao.deleteTag(state.value.currentTag)
                    appDao.upsertTag(action.tag)
                }
            }
            is LabelsTabAction.DismissTagDialog -> {
                _state.update { it.copy(
                    showTagDialog = false,
                    isEditingTag = false,
                    isAddingNewTag = false,
                ) }
            }
            is LabelsTabAction.DeleteTagClicked -> {
                _state.update { it.copy(showTagDialog = false) }
                viewModelScope.launch {
                    val trashed = action.tag.copy(inTrash = true)
                    appDao.upsertTag(trashed)
//                    Log.d(TAG, "TagsScreenAction.TrashTagSwiped $trashed")
                }
            }
            is LabelsTabAction.TagSortClicked -> {
                val pref = Preference(
                    key = PrefKey.TAG_SORT.name,
                    value = action.labelSort.name
                )
                viewModelScope.launch { appDao.upsertPreference(pref) }
            }
            /*
            PERSON
             */
            is LabelsTabAction.AddNewPersonClicked -> {
                _state.update { it.copy(
                    currentPerson = Label.Person(),
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
                viewModelScope.launch {
                    appDao.deletePerson(state.value.currentPerson)
                    appDao.upsertPerson(action.person)
                }
            }
            is LabelsTabAction.DismissPersonDialog -> {
                _state.update { it.copy(
                    showPersonDialog = false,
                    isEditingPerson = false,
                    isAddingNewPerson = false,
                ) }
            }
            is LabelsTabAction.DeletePersonClicked -> {
                _state.update { it.copy(showPersonDialog = false) }
                viewModelScope.launch {
                    val trashed = action.person.copy(inTrash = true)
                    appDao.upsertPerson(trashed)
                }
            }
            is LabelsTabAction.PersonSortClicked -> {
                val pref = Preference(
                    key = PrefKey.PERSON_SORT.name,
                    value = action.personSort.name
                )
                viewModelScope.launch { appDao.upsertPreference(pref) }
            }
            /*
            PLACE
             */
            is LabelsTabAction.AddNewPlaceClicked -> {
                _state.update { it.copy(
                    currentPlace = Label.Place(),
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
                viewModelScope.launch {
                    appDao.deletePlace(state.value.currentPlace)
                    appDao.upsertPlace(action.place)
                }
            }
            is LabelsTabAction.DismissPlaceDialog -> {
                _state.update { it.copy(
                    showPlaceDialog = false,
                    isEditingPlace = false,
                    isAddingNewPlace = false,
                ) }
            }
            is LabelsTabAction.DeletePlaceClicked -> {
                _state.update { it.copy(showPlaceDialog = false) }
                viewModelScope.launch {
                    val trashed = action.place.copy(inTrash = true)
                    appDao.upsertPlace(trashed)
                }
            }
            is LabelsTabAction.PlaceSortClicked -> {
                val pref = Preference(
                    key = PrefKey.PLACE_SORT.name,
                    value = action.placeSort.name
                )
                viewModelScope.launch { appDao.upsertPreference(pref) }
            }
        }
    }

    companion object {
        private const val TAG = "TagsScreenViewModel"
    }
}
package com.deslomator.tagtimer.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.LabelsTabAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Preference
import com.deslomator.tagtimer.model.type.PrefKey
import com.deslomator.tagtimer.model.type.LabelSort
import com.deslomator.tagtimer.model.type.LabelType
import com.deslomator.tagtimer.state.LabelsTabState
import com.deslomator.tagtimer.ui.theme.hue
import com.deslomator.tagtimer.util.combine
import com.deslomator.tagtimer.util.getSort
import com.deslomator.tagtimer.util.toColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LabelsTabViewModel @Inject constructor(
    private val appDao: AppDao,
) : ViewModel() {

    private val _state = MutableStateFlow(LabelsTabState())

    private val _tagSort = SortProvider.getTagSort(appDao, viewModelScope)
    private val _personSort = SortProvider.getPersonSort(appDao, viewModelScope)
    private val _placeSort = SortProvider.getPlaceSort(appDao, viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _tags = _tagSort.flatMapLatest { sort ->
        if (sort == LabelSort.NAME.sortId) appDao.getActiveLabels(LabelType.TAG.typeId)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getActiveLabels(LabelType.TAG.typeId)
            .map { lst -> lst.sortedBy { it.color.toColor().hue() } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _persons = _personSort.flatMapLatest { sort ->
        if (sort == LabelSort.NAME.sortId) {
            appDao.getActiveLabels(LabelType.PERSON.typeId)
                .map { lst -> lst.sortedBy { it.name } }
        } else {
            appDao.getActiveLabels(LabelType.PERSON.typeId)
                .map { lst -> lst.sortedBy { it.color.toColor().hue() } }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _places = _placeSort.flatMapLatest { sort ->
        if (sort == LabelSort.NAME.sortId) appDao.getActiveLabels(LabelType.PLACE.typeId)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getActiveLabels(LabelType.PLACE.typeId)
            .map { lst -> lst.sortedBy { it.color.toColor().hue() }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val state = combine(
        _state, _tags, _persons, _places, _tagSort, _personSort, _placeSort
    ) { state, tags, persons, places, tagSort, personSort, placeSort ->
        state.copy(
            tags = tags,
            persons = persons,
            places = places,
            tagSort = tagSort.getSort(),
            personSort = personSort.getSort(),
            placeSort = placeSort.getSort(),
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LabelsTabState())

    fun onAction(action: LabelsTabAction) {
        when (action) {
            /*
            TAG
             */
            is LabelsTabAction.AddNewTagClicked -> {
                _state.update {
                    it.copy(
                        currentTag = Label(type = LabelType.TAG.typeId),
                        showTagDialog = true,
                        isAddingNewTag = true
                    )
                }
            }

            is LabelsTabAction.EditTagClicked -> {
                _state.update {
                    it.copy(
                        currentTag = action.tag,
                        showTagDialog = true,
                        isEditingTag = true,
                    )
                }
            }

            is LabelsTabAction.AcceptTagEditionClicked -> {
                _state.update {
                    it.copy(
                        showTagDialog = false,
                        isEditingTag = false,
                    )
                }
                viewModelScope.launch {
                    appDao.upsertLabel(action.tag)
                }
            }

            is LabelsTabAction.DismissTagDialog -> {
                _state.update {
                    it.copy(
                        showTagDialog = false,
                        isEditingTag = false,
                        isAddingNewTag = false,
                    )
                }
            }
            // TODO show why a used label can't be deleted
            is LabelsTabAction.DeleteTagClicked -> {
                _state.update { it.copy(showTagDialog = false) }
                viewModelScope.launch {
                    val trashed = action.tag.copy(inTrash = true)
                    appDao.upsertLabel(trashed)
//                    Log.d(TAG, "TagsScreenAction.TrashTagSwiped $trashed")
                }
            }

            is LabelsTabAction.TagSortClicked -> {
                val pref = Preference(
                    prefKey = PrefKey.TAG_SORT.name,
                    value = action.labelSort.sortId
                )
                viewModelScope.launch { appDao.upsertPreference(pref) }
            }
            /*
            PERSON
             */
            is LabelsTabAction.AddNewPersonClicked -> {
                _state.update {
                    it.copy(
                        currentPerson = Label(type = LabelType.PERSON.typeId),
                        showPersonDialog = true,
                        isAddingNewPerson = true
                    )
                }
            }

            is LabelsTabAction.EditPersonClicked -> {
                _state.update {
                    it.copy(
                        currentPerson = action.person,
                        showPersonDialog = true,
                        isEditingPerson = true,
                    )
                }
            }

            is LabelsTabAction.AcceptPersonEditionClicked -> {
                _state.update {
                    it.copy(
                        showPersonDialog = false,
                        isEditingPerson = false,
                    )
                }
                viewModelScope.launch {
                    appDao.upsertLabel(action.person)
                }
            }

            is LabelsTabAction.DismissPersonDialog -> {
                _state.update {
                    it.copy(
                        showPersonDialog = false,
                        isEditingPerson = false,
                        isAddingNewPerson = false,
                    )
                }
            }
            // TODO show why a used label can't be deleted
            is LabelsTabAction.DeletePersonClicked -> {
                _state.update { it.copy(showPersonDialog = false) }
                viewModelScope.launch {
                    val trashed = action.person.copy(inTrash = true)
                    appDao.upsertLabel(trashed)
                }
            }

            is LabelsTabAction.PersonSortClicked -> {
                val pref = Preference(
                    prefKey = PrefKey.PERSON_SORT.name,
                    value = action.personSort.sortId
                )
                viewModelScope.launch { appDao.upsertPreference(pref) }
            }
            /*
            PLACE
             */
            is LabelsTabAction.AddNewPlaceClicked -> {
                _state.update {
                    it.copy(
                        currentPlace = Label(type = LabelType.PLACE.typeId),
                        showPlaceDialog = true,
                        isAddingNewPlace = true
                    )
                }
            }

            is LabelsTabAction.EditPlaceClicked -> {
                _state.update {
                    it.copy(
                        currentPlace = action.place,
                        showPlaceDialog = true,
                        isEditingPlace = true,
                    )
                }
            }

            is LabelsTabAction.AcceptPlaceEditionClicked -> {
                _state.update {
                    it.copy(
                        showPlaceDialog = false,
                        isEditingPlace = false,
                    )
                }
                viewModelScope.launch {
                    appDao.upsertLabel(action.place)
                }
            }

            is LabelsTabAction.DismissPlaceDialog -> {
                _state.update {
                    it.copy(
                        showPlaceDialog = false,
                        isEditingPlace = false,
                        isAddingNewPlace = false,
                    )
                }
            }
            // TODO show why a used label can't be deleted
            is LabelsTabAction.DeletePlaceClicked -> {
                _state.update { it.copy(showPlaceDialog = false) }
                viewModelScope.launch {
                    val trashed = action.place.copy(inTrash = true)
                    appDao.upsertLabel(trashed)
                }
            }

            is LabelsTabAction.PlaceSortClicked -> {
                val pref = Preference(
                    prefKey = PrefKey.PLACE_SORT.name,
                    value = action.placeSort.sortId
                )
                viewModelScope.launch { appDao.upsertPreference(pref) }
            }
        }
    }

    companion object {
        private const val TAG = "LabelsTabViewModel"
    }
}
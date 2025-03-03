package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.LabelsTabAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Preference
import com.deslomator.tagtimer.model.type.DialogState
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
            LABEL
             */
            is LabelsTabAction.AddNewLabelClicked -> {
                _state.update {
                    it.copy(
                        currentLabel = Label(type = action.type.typeId),
                        dialogState = DialogState.NEW_ITEM,
                    )
                }
            }

            is LabelsTabAction.EditLabelClicked -> {
                viewModelScope.launch {
                    val cbd = action.label.canBeDeleted(appDao)
                    _state.update {
                        it.copy(
                            currentLabel = action.label,
                            dialogState = if (cbd) DialogState.EDIT_CAN_DELETE else DialogState.EDIT_NO_DELETE,
                        )
                    }
                }
            }

            is LabelsTabAction.AcceptLabelEditionClicked -> {
                _state.update {
                    it.copy(
                        dialogState = DialogState.HIDDEN
                    )
                }
                viewModelScope.launch {
                    appDao.upsertLabel(action.label)
                }
            }

            is LabelsTabAction.DismissLabelDialog -> {
                _state.update {
                    it.copy(
                        dialogState = DialogState.HIDDEN
                    )
                }
            }

            is LabelsTabAction.DeleteTagClicked -> {
                _state.update { it.copy(
                    dialogState = DialogState.HIDDEN,
                ) }
                viewModelScope.launch {
                    val trashed = action.label.copy(inTrash = true)
                    appDao.upsertLabel(trashed)
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
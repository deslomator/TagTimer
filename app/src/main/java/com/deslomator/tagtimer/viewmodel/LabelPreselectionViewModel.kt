package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.LabelPreselectionAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Preference
import com.deslomator.tagtimer.model.Preselected
import com.deslomator.tagtimer.model.type.DialogState
import com.deslomator.tagtimer.model.type.LabelSort
import com.deslomator.tagtimer.model.type.LabelType
import com.deslomator.tagtimer.model.type.PrefKey
import com.deslomator.tagtimer.state.LabelPreselectionState
import com.deslomator.tagtimer.ui.theme.hue
import com.deslomator.tagtimer.util.combine
import com.deslomator.tagtimer.util.getSort
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
        if (sort == LabelSort.NAME.sortId) appDao.getActiveLabels(LabelType.TAG.typeId)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getActiveLabels(LabelType.TAG.typeId)
            .map { lst -> lst.sortedBy { it.color.toColor().hue() } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _preSelectedTags = _tagSort.flatMapLatest { sort ->
        if (sort == LabelSort.NAME.sortId) appDao.getPreSelectedLabelsForSession(_sessionId.value, LabelType.TAG.typeId)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getPreSelectedLabelsForSession(_sessionId.value, LabelType.TAG.typeId)
            .map { lst -> lst.sortedBy { it.color.toColor().hue() } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _persons = _personSort.flatMapLatest { sort ->
        if (sort == LabelSort.NAME.sortId) appDao.getActiveLabels(LabelType.PERSON.typeId)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getActiveLabels(LabelType.PERSON.typeId)
            .map { lst -> lst.sortedBy { it.color.toColor().hue() }
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _preSelectedPersons = _personSort.flatMapLatest { sort ->
        if (sort == LabelSort.NAME.sortId) appDao.getPreSelectedLabelsForSession(_sessionId.value, LabelType.PERSON.typeId)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getPreSelectedLabelsForSession(_sessionId.value, LabelType.PERSON.typeId)
            .map { lst -> lst.sortedBy { it.color.toColor().hue() }
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _preSelectedPlaces = _placeSort.flatMapLatest { sort ->
        if (sort == LabelSort.NAME.sortId) appDao.getPreSelectedLabelsForSession(_sessionId.value, LabelType.PLACE.typeId)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getPreSelectedLabelsForSession(_sessionId.value, LabelType.PLACE.typeId)
            .map { lst -> lst.sortedBy { it.color.toColor().hue() }
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val state = combine(
        _state, _preSelectedTags, _tags, _preSelectedPersons, _persons,
        _preSelectedPlaces, _places, _tagSort, _personSort, _placeSort
    ) { state, preSelectedTags, tags, preSelectedPersons, persons,
        preSelectedPlaces, places, tagSort, personSort, placeSort ->
        state.copy(
            preSelectedTags = preSelectedTags,
            tags = tags,
            preSelectedPersons = preSelectedPersons,
            persons = persons,
            preSelectedPlaces = preSelectedPlaces,
            places = places,
            tagSort = tagSort.getSort(),
            personSort = personSort.getSort(),
            placeSort = placeSort.getSort(),
        )
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), LabelPreselectionState()
    )

    fun onAction(action: LabelPreselectionAction) {
        when(action) {
            /*
            LABEL
             */
            is LabelPreselectionAction.AddNewLabelClicked -> {
                _state.update { it.copy(
                    currentLabel = Label(type = action.type.typeId),
                    dialogState = DialogState.NEW_ITEM,
                ) }
            }
            is LabelPreselectionAction.EditLabelClicked -> {
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
            is LabelPreselectionAction.AcceptLabelEditionClicked -> {
                _state.update { it.copy(
                    dialogState = DialogState.HIDDEN,
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
            is LabelPreselectionAction.DismissLabelDialog -> {
                _state.update { it.copy(
                    dialogState = DialogState.HIDDEN,
                ) }
            }

            is LabelPreselectionAction.DeleteLabelClicked -> {
                _state.update { it.copy(
                    dialogState = DialogState.HIDDEN,
                ) }
                viewModelScope.launch {
                    val trashed = action.label.copy(inTrash = true)
                    appDao.upsertLabel(trashed)
                }
            }

            is LabelPreselectionAction.ArchiveLabelClicked -> {
                val newValue = !action.label.archived
                _state.update { it.copy(
                    dialogState = DialogState.HIDDEN,
                ) }
                viewModelScope.launch {
                    val archived = action.label.copy(archived = newValue)
                    appDao.upsertLabel(archived)
                    // remove preselection if we are archiving a label
                    if (newValue) {
                        val pst = Preselected(
                            sessionId = _sessionId.value,
                            labelId = action.label.id!!
                        )
                        appDao.deletePreSelectedLabel(pst)
                    }
                }
            }
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
                        // unarchive the label if necessary
                        if (action.tag.archived) {
                            val lbl = action.tag.copy(archived = false)
                            appDao.upsertLabel(lbl)
                        }
                    } else {
                        appDao.deletePreSelectedLabel(pst)
                    }
                }
            }

            is LabelPreselectionAction.SortTagsClicked -> {
                val pref = Preference(
                    prefKey = PrefKey.TAG_SORT.name,
                    value = action.tagSort.sortId
                )
                viewModelScope.launch { appDao.upsertPreference(pref) }
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
                        // unarchive the label if necessary
                        if (action.person.archived) {
                            val lbl = action.person.copy(archived = false)
                            appDao.upsertLabel(lbl)
                        }
                    } else {
                        appDao.deletePreSelectedLabel(pst)
                    }
                }
            }

            is LabelPreselectionAction.SortPersonsClicked -> {
                val pref = Preference(
                    prefKey = PrefKey.PERSON_SORT.name,
                    value = action.personSort.sortId
                )
                viewModelScope.launch { appDao.upsertPreference(pref) }
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
                        // unarchive the label if necessary
                        if (action.place.archived) {
                            val lbl = action.place.copy(archived = false)
                            appDao.upsertLabel(lbl)
                        }
                    } else {
                        appDao.deletePreSelectedLabel(pst)
                    }
                }
            }

            is LabelPreselectionAction.SortPlacesClicked -> {
                val pref = Preference(
                    prefKey = PrefKey.PLACE_SORT.name,
                    value = action.placeSort.sortId
                )
                viewModelScope.launch { appDao.upsertPreference(pref) }
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
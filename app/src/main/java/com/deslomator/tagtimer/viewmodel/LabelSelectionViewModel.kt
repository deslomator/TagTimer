package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.LabelSelectionAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Preference
import com.deslomator.tagtimer.model.Selected
import com.deslomator.tagtimer.model.type.DialogState
import com.deslomator.tagtimer.model.type.ItemState
import com.deslomator.tagtimer.model.type.LabelSort
import com.deslomator.tagtimer.model.type.LabelType
import com.deslomator.tagtimer.model.type.PrefKey
import com.deslomator.tagtimer.model.type.PreferenceProvider
import com.deslomator.tagtimer.model.type.SessionSort
import com.deslomator.tagtimer.state.LabelSelectionState
import com.deslomator.tagtimer.ui.theme.hue
import com.deslomator.tagtimer.util.combine
import com.deslomator.tagtimer.util.toColor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LabelSelectionViewModel(
    private val appDao: AppDao,
    seshId: Long?
): ViewModel() {

    private val _sessionId = MutableStateFlow(0L)
    private val _state = MutableStateFlow(LabelSelectionState())

    private val _prefs = appDao.getPreferences()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _prefProvider = _prefs.mapLatest { prefs ->
        PreferenceProvider(prefs)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PreferenceProvider())

    /*@OptIn(ExperimentalCoroutinesApi::class)
    private val _tags = _prefProvider.flatMapLatest { prefProvider ->
        if (prefProvider.tagSort() == LabelSort.NAME) appDao.getActiveLabels(LabelType.TAG.typeId)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getActiveLabels(LabelType.TAG.typeId)
            .map { lst -> lst.sortedBy { it.color.toColor().hue() } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())*/


    @OptIn(ExperimentalCoroutinesApi::class)
    private val _tags = _prefProvider.flatMapLatest { prefProvider ->
//        Log.d(TAG, "choosing sorted session list")
        appDao.getLabels(LabelType.TAG.typeId).map { labels ->
            val enabled = if (prefProvider.showEnabledLabels()) labels.filter { it.type == LabelType.TAG.typeId && it.state == ItemState.ENABLED } else emptyList()
            val archived = if (prefProvider.showArchivedLabels()) labels.filter {  it.type == LabelType.TAG.typeId && it.state == ItemState.ARCHIVED } else emptyList()
            val trashed = if (prefProvider.showTrashedLabels()) labels.filter {  it.type == LabelType.TAG.typeId && it.state == ItemState.TRASHED } else emptyList()
            (enabled + archived + trashed).run {
                when (prefProvider.tagSort()) {
                    LabelSort.NAME -> sortedBy { it.name }
                    LabelSort.COLOR -> sortedBy { it.color.toColor().hue() }
                }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _persons = _prefProvider.flatMapLatest { prefProvider ->
//        Log.d(TAG, "choosing sorted session list")
        appDao.getLabels(LabelType.PERSON.typeId).map { labels ->
            val enabled = if (prefProvider.showEnabledLabels()) labels.filter {  it.type == LabelType.PERSON.typeId && it.state == ItemState.ENABLED } else emptyList()
            val archived = if (prefProvider.showArchivedLabels()) labels.filter { it.type == LabelType.PERSON.typeId && it.state == ItemState.ARCHIVED } else emptyList()
            val trashed = if (prefProvider.showTrashedLabels()) labels.filter { it.type == LabelType.PERSON.typeId && it.state == ItemState.TRASHED } else emptyList()
            (enabled + archived + trashed).run {
                when (prefProvider.tagSort()) {
                    LabelSort.NAME -> sortedBy { it.name }
                    LabelSort.COLOR -> sortedBy { it.color.toColor().hue() }
                }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _places = _prefProvider.flatMapLatest { prefProvider ->
//        Log.d(TAG, "choosing sorted session list")
        appDao.getLabels(LabelType.PLACE.typeId).map { labels ->
            val enabled = if (prefProvider.showEnabledLabels()) labels.filter { it.type == LabelType.PLACE.typeId && it.state == ItemState.ENABLED } else emptyList()
            val archived = if (prefProvider.showArchivedLabels()) labels.filter { it.type == LabelType.PLACE.typeId && it.state == ItemState.ARCHIVED } else emptyList()
            val trashed = if (prefProvider.showTrashedLabels()) labels.filter { it.type == LabelType.PLACE.typeId && it.state == ItemState.TRASHED } else emptyList()
            (enabled + archived + trashed).run {
                when (prefProvider.tagSort()) {
                    LabelSort.NAME -> sortedBy { it.name }
                    LabelSort.COLOR -> sortedBy { it.color.toColor().hue() }
                }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())





    @OptIn(ExperimentalCoroutinesApi::class)
    private val _selectedTags = _prefProvider.flatMapLatest { prefProvider ->
        if (prefProvider.tagSort() == LabelSort.NAME) appDao.getSelectedLabelsForSession(_sessionId.value, LabelType.TAG.typeId)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getSelectedLabelsForSession(_sessionId.value, LabelType.TAG.typeId)
            .map { lst -> lst.sortedBy { it.color.toColor().hue() } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /*@OptIn(ExperimentalCoroutinesApi::class)
    private val _persons = _prefProvider.flatMapLatest { prefProvider ->
        if (prefProvider.personSort() == LabelSort.NAME) appDao.getActiveLabels(LabelType.PERSON.typeId)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getActiveLabels(LabelType.PERSON.typeId)
            .map { lst -> lst.sortedBy { it.color.toColor().hue() }
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())*/

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _selectedPersons = _prefProvider.flatMapLatest { prefProvider ->
        if (prefProvider.personSort() == LabelSort.NAME) appDao.getSelectedLabelsForSession(_sessionId.value, LabelType.PERSON.typeId)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getSelectedLabelsForSession(_sessionId.value, LabelType.PERSON.typeId)
            .map { lst -> lst.sortedBy { it.color.toColor().hue() }
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /*@OptIn(ExperimentalCoroutinesApi::class)
    private val _places = _prefProvider.flatMapLatest { prefProvider ->
        if (prefProvider.placeSort() == LabelSort.NAME) appDao.getActiveLabels(LabelType.PLACE.typeId)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getActiveLabels(LabelType.PLACE.typeId)
            .map { lst -> lst.sortedBy { it.color.toColor().hue() }
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())*/

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _selectedPlaces = _prefProvider.flatMapLatest { prefProvider ->
        if (prefProvider.placeSort() == LabelSort.NAME) appDao.getSelectedLabelsForSession(_sessionId.value, LabelType.PLACE.typeId)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getSelectedLabelsForSession(_sessionId.value, LabelType.PLACE.typeId)
            .map { lst -> lst.sortedBy { it.color.toColor().hue() }
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val state = combine(
        _state, _selectedTags, _tags, _selectedPersons, _persons,
        _selectedPlaces, _places, _prefProvider
    ) { state, selectedTags, tags, selectedPersons, persons,
        selectedPlaces, places, prefProvider ->
        state.copy(
            selectedTags = selectedTags,
            tags = tags,
            selectedPersons = selectedPersons,
            persons = persons,
            selectedPlaces = selectedPlaces,
            places = places,
            preferenceProvider = prefProvider,
        )
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), LabelSelectionState()
    )

    fun onAction(action: LabelSelectionAction) {
        when(action) {
            /*
            LABEL
             */
            is LabelSelectionAction.AddNewLabelClicked -> {
                _state.update { it.copy(
                    currentLabel = Label(type = action.type.typeId),
                    dialogState = DialogState.NEW_ITEM,
                ) }
            }
            is LabelSelectionAction.EditLabelClicked -> {
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
            is LabelSelectionAction.AcceptLabelEditionClicked -> {
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
            is LabelSelectionAction.DismissLabelDialog -> {
                _state.update { it.copy(
                    dialogState = DialogState.HIDDEN,
                ) }
            }

            is LabelSelectionAction.DeleteLabelClicked -> {
                _state.update { it.copy(
                    dialogState = DialogState.HIDDEN,
                ) }
                viewModelScope.launch {
                    val trashed = action.label.copy(state = ItemState.TRASHED)
                    appDao.upsertLabel(trashed)
                }
            }

            is LabelSelectionAction.ArchiveLabelClicked -> {
                val newValue = if (action.label.state == ItemState.ARCHIVED) ItemState.ENABLED else ItemState.ARCHIVED
                _state.update { it.copy(
                    dialogState = DialogState.HIDDEN,
                ) }
                viewModelScope.launch {
                    val archived = action.label.copy(state = newValue)
                    appDao.upsertLabel(archived)
                    // remove selection if we are archiving a label
                    if (newValue == ItemState.ARCHIVED) {
                        val pst = Selected(
                            sessionId = _sessionId.value,
                            labelId = action.label.id!!
                        )
                        appDao.deleteSelectedLabel(pst)
                    }
                }
            }
            /*
            TAG
             */
            is LabelSelectionAction.SelectTagCheckedChange -> {
                viewModelScope.launch {
                    val pst = Selected(
                        sessionId = _sessionId.value,
                        labelId = action.tag.id!!
                    )
                    delay(UPSERT_DELAY_MS) // TODO what's this delay
                    if (action.checked) {
                        appDao.upsertSelectedLabel(pst)
                        // unarchive the label if necessary
                        if (action.tag.state == ItemState.ARCHIVED) {
                            val lbl = action.tag.copy(state = ItemState.ENABLED)
                            appDao.upsertLabel(lbl)
                        }
                    } else {
                        appDao.deleteSelectedLabel(pst)
                    }
                }
            }

            is LabelSelectionAction.SortTagsClicked -> {
                val pref = Preference(
                    prefKey = PrefKey.TAG_SORT,
                    value = action.tagSort.name
                )
                viewModelScope.launch { appDao.upsertPreference(pref) }
            }
            /*
            PERSON
             */
            is LabelSelectionAction.SelectPersonCheckedChange -> {
                viewModelScope.launch {
                    val pst = Selected(
                        sessionId = _sessionId.value,
                        labelId = action.person.id!!
                    )
                    delay(UPSERT_DELAY_MS) // TODO what's this delay
                    if (action.checked) {
                        appDao.upsertSelectedLabel(pst)
                        // unarchive the label if necessary
                        if (action.person.state == ItemState.ARCHIVED) {
                            val lbl = action.person.copy(state = ItemState.ENABLED)
                            appDao.upsertLabel(lbl)
                        }
                    } else {
                        appDao.deleteSelectedLabel(pst)
                    }
                }
            }

            is LabelSelectionAction.SortPersonsClicked -> {
                val pref = Preference(
                    prefKey = PrefKey.PERSON_SORT,
                    value = action.personSort.name
                )
                viewModelScope.launch { appDao.upsertPreference(pref) }
            }
            /*
            PLACE
             */
            is LabelSelectionAction.SelectPlaceCheckedChange -> {
                viewModelScope.launch {
                    val pst = Selected(
                        sessionId = _sessionId.value,
                        labelId = action.place.id!!
                    )
                    delay(UPSERT_DELAY_MS) // TODO what's this delay
                    if (action.checked) {
                        appDao.upsertSelectedLabel(pst)
                        // unarchive the label if necessary
                        if (action.place.state == ItemState.ARCHIVED) {
                            val lbl = action.place.copy(state = ItemState.ENABLED)
                            appDao.upsertLabel(lbl)
                        }
                    } else {
                        appDao.deleteSelectedLabel(pst)
                    }
                }
            }

            is LabelSelectionAction.SortPlacesClicked -> {
                val pref = Preference(
                    prefKey = PrefKey.PLACE_SORT,
                    value = action.placeSort.name
                )
                viewModelScope.launch { appDao.upsertPreference(pref) }
            }

            is LabelSelectionAction.ShowEnabledClicked -> {
                viewModelScope.launch {
                    appDao.upsertPreference(Preference(PrefKey.SHOW_ENABLED_LABELS, action.show.toString()))
                }
            }

            is LabelSelectionAction.ShowArchivedClicked -> {
                viewModelScope.launch {
                    appDao.upsertPreference(Preference(PrefKey.SHOW_ARCHIVED_LABELS, action.show.toString()))
                }
            }

            is LabelSelectionAction.ShowTrashedClicked -> {
                viewModelScope.launch {
                    appDao.upsertPreference(Preference(PrefKey.SHOW_TRASHED_LABELS, action.show.toString()))
                }
            }
        }
    }

    init {
        val sessionId = seshId ?: 0
        _sessionId.update { sessionId }
        viewModelScope.launch {
            _state.update {
                it.copy(currentSession = appDao.getSession(sessionId))
            }
        }
    }

    companion object {
        private const val UPSERT_DELAY_MS = 300L
        private const val TAG = "LabelSelectionViewModel"
    }
}
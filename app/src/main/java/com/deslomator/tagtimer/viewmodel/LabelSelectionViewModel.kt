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
import com.deslomator.tagtimer.model.ancillary.PreferenceProvider
import com.deslomator.tagtimer.state.LabelSelectionState
import com.deslomator.tagtimer.ui.theme.hue
import com.deslomator.tagtimer.util.combine
import com.deslomator.tagtimer.util.toColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
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
    private val sessionId: Long
): ViewModel() {

    private val _state = MutableStateFlow(LabelSelectionState())

    private val _prefs = appDao.getPreferences()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _prefProvider = _prefs.mapLatest { prefs ->
        PreferenceProvider(prefs)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), PreferenceProvider())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _tags = _prefProvider.flatMapLatest { prefProvider ->
//        Log.d(TAG, "choosing sorted session list")
        appDao.getTags().map { labels ->
            val enabled = if (prefProvider.showEnabledLabels()) labels.filter { it.type == LabelType.TAG && it.state == ItemState.ENABLED } else emptyList()
            val archived = if (prefProvider.showArchivedLabels()) labels.filter {  it.type == LabelType.TAG && it.state == ItemState.ARCHIVED } else emptyList()
            val trashed = if (prefProvider.showTrashedLabels()) labels.filter {  it.type == LabelType.TAG && it.state == ItemState.TRASHED } else emptyList()
            (enabled + archived + trashed).run {
                when (prefProvider.tagSort()) {
                    LabelSort.NAME -> sortedBy { it.name }
                    LabelSort.COLOR -> sortedBy { it.color.toColor().hue() }
                }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _persons = _prefProvider.flatMapLatest { prefProvider ->
//        Log.d(TAG, "choosing sorted session list")
        appDao.getPersons().map { labels ->
            val enabled = if (prefProvider.showEnabledLabels()) labels.filter {  it.type == LabelType.PERSON && it.state == ItemState.ENABLED } else emptyList()
            val archived = if (prefProvider.showArchivedLabels()) labels.filter { it.type == LabelType.PERSON && it.state == ItemState.ARCHIVED } else emptyList()
            val trashed = if (prefProvider.showTrashedLabels()) labels.filter { it.type == LabelType.PERSON && it.state == ItemState.TRASHED } else emptyList()
            (enabled + archived + trashed).run {
                when (prefProvider.personSort()) {
                    LabelSort.NAME -> sortedBy { it.name }
                    LabelSort.COLOR -> sortedBy { it.color.toColor().hue() }
                }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _places = _prefProvider.flatMapLatest { prefProvider ->
//        Log.d(TAG, "choosing sorted session list")
        appDao.getPLaces().map { labels ->
            val enabled = if (prefProvider.showEnabledLabels()) labels.filter { it.type == LabelType.PLACE && it.state == ItemState.ENABLED } else emptyList()
            val archived = if (prefProvider.showArchivedLabels()) labels.filter { it.type == LabelType.PLACE && it.state == ItemState.ARCHIVED } else emptyList()
            val trashed = if (prefProvider.showTrashedLabels()) labels.filter { it.type == LabelType.PLACE && it.state == ItemState.TRASHED } else emptyList()
            (enabled + archived + trashed).run {
                when (prefProvider.placeSort()) {
                    LabelSort.NAME -> sortedBy { it.name }
                    LabelSort.COLOR -> sortedBy { it.color.toColor().hue() }
                }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _selectedTags = _prefProvider.flatMapLatest { prefProvider ->
        if (prefProvider.tagSort() == LabelSort.NAME) appDao.getSelectedTagsForSession(sessionId)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getSelectedTagsForSession(sessionId)
            .map { lst -> lst.sortedBy { it.color.toColor().hue() } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _selectedPersons = _prefProvider.flatMapLatest { prefProvider ->
        if (prefProvider.personSort() == LabelSort.NAME) appDao.getSelectedPersonsForSession(sessionId)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getSelectedPersonsForSession(sessionId)
            .map { lst -> lst.sortedBy { it.color.toColor().hue() }
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _selectedPlaces = _prefProvider.flatMapLatest { prefProvider ->
        if (prefProvider.placeSort() == LabelSort.NAME) appDao.getSelectedPlacesForSession(sessionId)
            .map { lst -> lst.sortedBy { it.name } }
        else appDao.getSelectedPlacesForSession(sessionId)
            .map { lst -> lst.sortedBy { it.color.toColor().hue() }
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

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
                    currentLabel = Label(type = action.type),
                    dialogState = DialogState.NEW_ITEM,
                ) }
            }

            is LabelSelectionAction.EditLabelClicked -> {
                val dialogState = when (action.label.state) {
                    ItemState.ENABLED -> DialogState.ENABLED
                    ItemState.ARCHIVED -> DialogState.ARCHIVED
                    ItemState.TRASHED -> DialogState.TRASHED
                }
                viewModelScope.launch {
                    val cbd = async(Dispatchers.IO) {
                        action.label.canBeDeleted(appDao)
                    }.await()
                    _state.update {
                        it.copy(
                            currentLabel = action.label,
                            canBeDeleted = cbd,
                            dialogState = dialogState,
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
                viewModelScope.launch(Dispatchers.IO) {
                    appDao.upsertLabel(edited)
                }
            }

            is LabelSelectionAction.DismissLabelDialog -> {
                _state.update { it.copy(
                    dialogState = DialogState.HIDDEN,
                ) }
            }

            is LabelSelectionAction.ArchiveLabelClicked -> {
                _state.update { it.copy(
                    dialogState = DialogState.HIDDEN,
                ) }
                viewModelScope.launch(Dispatchers.IO) {
                    val archived = state.value.currentLabel.copy(state = ItemState.ARCHIVED)
                    appDao.upsertLabel(archived)
                    // remove selection
                    val selected = Selected(
                        sessionId = sessionId,
                        labelId = state.value.currentLabel.id!!
                    )
                    appDao.deleteSelected(selected)
                }
            }

            is LabelSelectionAction.UnArchiveLabelClicked -> {
                _state.update { it.copy(
                    dialogState = DialogState.HIDDEN,
                ) }
                viewModelScope.launch(Dispatchers.IO) {
                    val unArchived = state.value.currentLabel.copy(state = ItemState.ENABLED)
                    appDao.upsertLabel(unArchived)
                }
            }

            is LabelSelectionAction.TrashLabelClicked -> {
                val cur = state.value.currentLabel
                _state.update { it.copy(
                    dialogState = DialogState.HIDDEN,
                ) }
                viewModelScope.launch(Dispatchers.IO) {
                    val cbd = cur.canBeDeleted(appDao)
                    if (cbd) {
                        val trashed = cur.copy(state = ItemState.TRASHED)
                        appDao.upsertLabel(trashed)
                        // remove selection
                        val selected = Selected(
                            sessionId = sessionId,
                            labelId = state.value.currentLabel.id!!
                        )
                        appDao.deleteSelected(selected)
                    } else {
                        _state.update { it.copy(showMessage = true) }
                    }
                }
            }

            is LabelSelectionAction.UnTrashLabelClicked -> {
                _state.update { it.copy(
                    dialogState = DialogState.HIDDEN,
                ) }
                viewModelScope.launch(Dispatchers.IO) {
                    val unTrashed = state.value.currentLabel.copy(state = ItemState.ENABLED)
                    appDao.upsertLabel(unTrashed)
                }
            }

            is LabelSelectionAction.PurgeLabelClicked -> {
                _state.update { it.copy(
                    dialogState = DialogState.HIDDEN,
                ) }
                viewModelScope.launch(Dispatchers.IO) {
                    val cbd = state.value.currentLabel.canBeDeleted(appDao)
                    if (cbd) appDao.deleteLabel(state.value.currentLabel)
                }
            }

            is LabelSelectionAction.SelectLabelCheckedChange -> {
                viewModelScope.launch {
                    val pst = Selected(
                        sessionId = sessionId,
                        labelId = action.label.id!!
                    )
                    if (action.checked) {
                        launch(Dispatchers.IO) { appDao.upsertSelected(pst) }
                        // unarchive-untrash the label if necessary
                        if (action.label.state != ItemState.ENABLED) {
                            val checked = action.label.copy(state = ItemState.ENABLED)
                            launch(Dispatchers.IO) { appDao.upsertLabel(checked) }
                        }
                    } else {
                        launch(Dispatchers.IO) { appDao.deleteSelected(pst) }
                    }
                }
            }
            /*
            TAG
             */
            is LabelSelectionAction.SortTagsClicked -> {
                val pref = Preference(
                    prefKey = PrefKey.TAG_SORT,
                    value = action.tagSort.name
                )
                viewModelScope.launch(Dispatchers.IO) { appDao.upsertPreference(pref) }
            }
            /*
            PERSON
             */
            is LabelSelectionAction.SortPersonsClicked -> {
                val pref = Preference(
                    prefKey = PrefKey.PERSON_SORT,
                    value = action.personSort.name
                )
                viewModelScope.launch(Dispatchers.IO) { appDao.upsertPreference(pref) }
            }
            /*
            PLACE
             */
            is LabelSelectionAction.SortPlacesClicked -> {
                val pref = Preference(
                    prefKey = PrefKey.PLACE_SORT,
                    value = action.placeSort.name
                )
                viewModelScope.launch(Dispatchers.IO) { appDao.upsertPreference(pref) }
            }

            is LabelSelectionAction.ShowEnabledClicked -> {
                viewModelScope.launch(Dispatchers.IO) {
                    appDao.upsertPreference(Preference(PrefKey.SHOW_ENABLED_LABELS, action.show.toString()))
                }
            }

            is LabelSelectionAction.ShowArchivedClicked -> {
                viewModelScope.launch(Dispatchers.IO) {
                    appDao.upsertPreference(Preference(PrefKey.SHOW_ARCHIVED_LABELS, action.show.toString()))
                }
            }

            is LabelSelectionAction.ShowTrashedClicked -> {
                viewModelScope.launch(Dispatchers.IO) {
                    appDao.upsertPreference(Preference(PrefKey.SHOW_TRASHED_LABELS, action.show.toString()))
                }
            }

            is LabelSelectionAction.DismissDeleteDialog -> {
                _state.update { it.copy(showMessage = false) }
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
        private const val TAG = "LabelSelectionViewModel"
    }
}
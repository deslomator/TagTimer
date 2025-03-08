package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.TrashTabAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.type.ItemState
import com.deslomator.tagtimer.state.TrashTabState
import com.deslomator.tagtimer.util.combine
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TrashTabViewModel(
    private val appDao: AppDao,
    sessionId: Long
): ViewModel() {

    private val _state = MutableStateFlow(TrashTabState())

    private val _trashedEvents = appDao.getTrashedEventsForDisplay(sessionId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _sessions = appDao.getTrashedSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _persons = appDao.getTrashedPersons()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _places = appDao.getTrashedPlaces()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _tags = appDao.getTrashedTags()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val state = combine(_state, _trashedEvents, _sessions, _tags, _persons, _places) {
            state, trashedEvents, sessions, tags, persons, places ->
        state.copy(
            sessions = sessions,
            trashedEvents = trashedEvents,
            tags = tags,
            persons = persons,
            places = places
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TrashTabState())

    fun onAction(action: TrashTabAction) {
        when(action) {
            /*
            SESSION
             */
            is TrashTabAction.DeleteSessionClicked -> {
                viewModelScope.launch { appDao.deleteSession(action.session) }
            }
            is TrashTabAction.RestoreSessionClicked -> {
                viewModelScope.launch {
                    val trashed = action.session.copy(state = ItemState.ENABLED)
                    appDao.upsertSession(trashed)
                }
            }
            /*
            LABEL
             */
            is TrashTabAction.DeleteLabelClicked -> {
                viewModelScope.launch {
                    appDao.deleteLabel(action.tag)
                }
            }
            is TrashTabAction.RestoreLabelClicked -> {
                viewModelScope.launch {
                    val trashed = action.tag.copy(state = ItemState.TRASHED)
                    appDao.upsertLabel(trashed)
                }
            }
            /*
            EVENT
             */
            is TrashTabAction.DeleteEventClicked -> {
                viewModelScope.launch { appDao.deleteEvent(action.event4d.event) }
            }
            is TrashTabAction.RestoreEventClicked -> {
                viewModelScope.launch {
                    val e = action.event4d.event.copy(inTrash = false)
                    appDao.upsertEvent(e) }
            }
            is TrashTabAction.EventInTrashClicked -> {
                _state.update { it.copy(
                    eventForDialog = action.event,
                    showEventInTrashDialog = true
                ) }
            }

            is TrashTabAction.DismissEventInTrashDialog -> {
                _state.update { it.copy(showEventInTrashDialog = false) }
            }
        }
    }

    init {

        viewModelScope.launch {
            _state.update {
                it.copy(currentSession = appDao.getSession(sessionId))
            }
        }
    }

    companion object {
        private const val TAG = "SessionsTrashViewModel"
    }
}
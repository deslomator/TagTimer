package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.TrashTabAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.state.TrashTabState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TrashTabViewModel(
    private val appDao: AppDao,
    sessionId: Long
): ViewModel() {

    private val _state = MutableStateFlow(TrashTabState())

    private val _trashedEvents = appDao.getTrashedEventsForDisplay(sessionId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = combine(_state, _trashedEvents) {
            state, trashedEvents ->
        state.copy(
            trashedEvents = trashedEvents,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TrashTabState())

    fun onAction(action: TrashTabAction) {
        when(action) {
            /*
            EVENT
             */
            is TrashTabAction.DeleteEventClicked -> {
                viewModelScope.launch(Dispatchers.IO) {
                    appDao.deleteEvent(action.event4d.event)
                }
            }

            is TrashTabAction.RestoreEventClicked -> {
                viewModelScope.launch(Dispatchers.IO) {
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

        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(currentSession = appDao.getSession(sessionId))
            }
        }
    }

    companion object {
        private const val TAG = "TrashTabViewModel"
    }
}
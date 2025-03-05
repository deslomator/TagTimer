package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.EventTrashAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.state.EventTrashState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EventTrashViewModel(
    private val appDao: AppDao,
    seshId: Long?
): ViewModel() {

    private val _sessionId = MutableStateFlow(0L)
    private val _state = MutableStateFlow(EventTrashState())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _trashedEvents = _sessionId
        .flatMapLatest {
            appDao.getTrashedEventsForDisplay(_sessionId.value)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val state = combine(
        _state,
        _trashedEvents
    ) { state, trashedEvents ->
        state.copy(
            trashedEvents = trashedEvents,
        )
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), EventTrashState()
    )

    fun onAction(action: EventTrashAction) {
        when(action) {
            is EventTrashAction.DeleteEventClicked -> {
                viewModelScope.launch { appDao.deleteEvent(action.event4d.event) }
            }
            is EventTrashAction.RestoreEventClicked -> {
                viewModelScope.launch {
                    val e = action.event4d.event.copy(inTrash = false)
                    appDao.upsertEvent(e) }
            }
            is EventTrashAction.EventInTrashClicked -> {
                _state.update { it.copy(
                    eventForDialog = action.event,
                    showEventInTrashDialog = true
                ) }
            }

            is EventTrashAction.DismissEventInTrashDialog -> {
                _state.update { it.copy(showEventInTrashDialog = false) }
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
        private const val TAG = "EventTrashViewModel"
    }
}
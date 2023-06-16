package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.SessionsTrashAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.state.AppState
import com.deslomator.tagtimer.state.SessionsTrashState
import com.deslomator.tagtimer.ui.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SessionsTrashViewModel(
    private val appDao: AppDao,
    ): ViewModel() {

    private val _state = MutableStateFlow(SessionsTrashState())
    private val _sessions = appDao.getTrashedSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val state = combine(_state, _sessions) { state, sessions ->
        state.copy(
            sessions = sessions,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SessionsTrashState())

    fun onAction(action: SessionsTrashAction) {
        when(action) {
            is SessionsTrashAction.DeleteSessionClicked -> {
                viewModelScope.launch {
                    appDao.deleteSession(action.session)
                    appDao.deleteEventsForSession(action.session.id)
                }
            }
        }
    }

    companion object {
        private const val TAG = "SessionsTrashViewModel"
    }
}
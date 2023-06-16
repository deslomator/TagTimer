package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.SessionsTrashAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.state.AppState
import com.deslomator.tagtimer.state.SessionsScreenState
import com.deslomator.tagtimer.state.SessionsTrashState
import com.deslomator.tagtimer.ui.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class SessionsTrashViewModel(
    private val appDao: AppDao,
    appState: AppState
    ): ViewModel() {

    private val _appState = MutableStateFlow(appState)
    /**
     * SessionsScreen
     */
    private val _state = MutableStateFlow(SessionsTrashState())
    private val _sessions = appDao.getTrashedSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val state = combine(_state, _sessions, _appState) { state, sessions, appState ->
        state.copy(
            sessions = sessions,
            activeScreen = appState.currentScreen
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SessionsScreenState())

    fun onAction(action: SessionsTrashAction) {
        when(action) {
            SessionsTrashAction.BackClicked -> {
                _state.update { it.copy(activeScreen = Screen.SESSIONS) }
            }
        }
    }

    companion object {
        private const val TAG = "SessionsTrashViewModel"
    }
}
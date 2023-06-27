package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.SessionsTabAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.state.SessionsTabState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionsTabViewModel @Inject constructor(
    private val appDao: AppDao,
): ViewModel() {

    private val _state = MutableStateFlow(SessionsTabState())
    private val _sessions = appDao.getActiveSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val state = combine(_state, _sessions) { state, sessions ->
        state.copy(
            sessions = sessions,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SessionsTabState())

    fun onAction(action: SessionsTabAction) {
        when(action) {
            SessionsTabAction.AddNewSessionClicked -> {
                _state.update { it.copy(
                    sessionColor = it.currentSession.color,
                    showSessionDialog = true,
                ) }
            }
            is SessionsTabAction.AcceptAddSessionClicked -> {
                val session = Session(
                    name = state.value.sessionName,
                    color = state.value.sessionColor,
                    lastAccessMillis = System.currentTimeMillis()
                )
                _state.update { it.copy(
                    showSessionDialog = false,
                    sessionColor = 0,
                    sessionName = ""
                ) }
                viewModelScope.launch { appDao.upsertSession(session) }
            }
            is SessionsTabAction.DismissSessionDialog -> {
                _state.update { it.copy(
                    showSessionDialog = false,
                    sessionColor = 0,
                    sessionName = ""
                ) }
            }
            is SessionsTabAction.UpdateSessionName -> {
                _state.update { it.copy(sessionName = action.name) }
            }
            is SessionsTabAction.UpdateSessionColor -> {
                _state.update { it.copy(sessionColor = action.color) }
            }
            is SessionsTabAction.TrashSessionSwiped -> {
                viewModelScope.launch {
                    val trashed = action.session.copy(inTrash = true)
                    appDao.upsertSession(trashed)
//                    Log.d(TAG, "SessionsScreenAction.TrashSessionSwiped $trashed")
                }
            }
        }
    }

    companion object {
        private const val TAG = "SessionsScreenViewModel"
    }
}
package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.SessionsScreenAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.state.AppState
import com.deslomator.tagtimer.state.SessionsScreenState
import com.deslomator.tagtimer.ui.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SessionsScreenViewModel(
    private val appDao: AppDao,
    appState: AppState
    ): ViewModel() {

    private val _appState = MutableStateFlow(appState)
    /**
     * SessionsScreen
     */
    private val _state = MutableStateFlow(SessionsScreenState())
    private val _sessions = appDao.getActiveSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val state = combine(_state, _sessions, _appState) { state, sessions, appState ->
        state.copy(
            sessions = sessions,
            activeScreen = appState.currentScreen
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SessionsScreenState())

    fun onAction(action: SessionsScreenAction) {
        when(action) {
            SessionsScreenAction.AddNewSessionClicked -> {
                _state.update { it.copy(
                    sessionColor = it.currentSession.color,
                    showSessionDialog = true,
                    isAddingNewSession = true
                ) }
            }
            is SessionsScreenAction.AcceptAddNewSessionClicked -> {
                val session = Session(
                    name = state.value.sessionName,
                    color = state.value.sessionColor,
                    lastAccessMillis = System.currentTimeMillis()
                )
                _state.update { it.copy(
                    showSessionDialog = false,
                    isAddingNewSession = false,
                    sessionColor = 0,
                    sessionName = ""
                ) }
                viewModelScope.launch { appDao.upsertSession(session) }
            }
            is SessionsScreenAction.EditSessionClicked -> {
                _state.update { it.copy(
                    showSessionDialog = true,
                    isEditingSession = true,
                    lastAccessMillis = System.currentTimeMillis(),
                    currentSession = action.session,
                    sessionColor = action.session.color,
                    sessionName = action.session.name
                ) }
            }
            is SessionsScreenAction.AcceptSessionEditionClicked -> {
                val session = Session(
                    id = state.value.currentSession.id,
                    startTimeMillis = action.session.startTimeMillis,
                    endTimeMillis = action.session.endTimeMillis,
                    name = state.value.sessionName,
                    color = state.value.sessionColor,
                    lastAccessMillis = System.currentTimeMillis()
                )
//                Log.d(TAG, "editing session, original id: ${state.value.currentSession.id}")
//                Log.d(TAG, "editing session, new id: ${session.id}")
                _state.update { it.copy(
                    showSessionDialog = false,
                    isEditingSession = false,
                    sessionColor = 0,
                    sessionName = "",
                    currentSession = Session()
                ) }
                viewModelScope.launch { appDao.upsertSession(session) }
            }
            is SessionsScreenAction.DismissSessionDialog -> {
                _state.update { it.copy(
                    showSessionDialog = false,
                    isEditingSession = false,
                    isAddingNewSession = false,
                    sessionColor = 0,
                    sessionName = ""
                ) }
            }
            is SessionsScreenAction.UpdateSessionName -> {
                _state.update { it.copy(sessionName = action.name) }
            }
            is SessionsScreenAction.UpdateSessionColor -> {
                _state.update { it.copy(sessionColor = action.color) }
            }
            is SessionsScreenAction.DeleteSessionSwiped -> {
                _state.update { it.copy(showSessionDeleteSnackbar = true) }
                viewModelScope.launch {
                    appDao.deleteSession(action.session)
                    appDao.deleteEventsForSession(action.session.id)
                }
            }
            is SessionsScreenAction.HideSessionDeletedSnackbar -> {
                _state.update { it.copy(showSessionDeleteSnackbar = false) }
            }
            is SessionsScreenAction.SessionItemClicked -> {
                //TODO
            }
            is SessionsScreenAction.ManageTagsClicked -> TODO()
            SessionsScreenAction.SessionsTrashClicked -> {
                _state.update { it.copy(activeScreen = Screen.SESSIONS_TRASH) }
            }
        }
    }

    companion object {
        private const val TAG = "SessionsScreenViewModel"
    }
}
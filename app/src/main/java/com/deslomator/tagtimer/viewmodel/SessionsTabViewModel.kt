package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.SessionsTabAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.populateDb
import com.deslomator.tagtimer.state.SessionsTabState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
                    currentSession = Session(),
                    isEditingSession = false,
                    showSessionDialog = true,
                ) }
            }
            is SessionsTabAction.ItemClicked -> {
                _state.update { it.copy(
                    currentSession = action.session,
                    isEditingSession = true,
                    showSessionDialog = true
                ) }
            }
            is SessionsTabAction.DialogAcceptClicked -> {
                _state.update { it.copy(showSessionDialog = false) }
                viewModelScope.launch { appDao.upsertSession(action.session) }
            }
            is SessionsTabAction.DismissSessionDialog -> {
                _state.update { it.copy(showSessionDialog = false) }
            }
            is SessionsTabAction.TrashSessionClicked -> {
                viewModelScope.launch {
                    _state.update { it.copy(showSessionDialog = false) }
                    val trashed = state.value.currentSession.copy(inTrash = true)
                    appDao.upsertSession(trashed)
//                    Log.d(TAG, "SessionsScreenAction.TrashSessionSwiped $trashed")
                }
            }
            SessionsTabAction.PopulateDbClicked -> {
                viewModelScope.launch { populateDb(appDao) }
            }
            is SessionsTabAction.CopySessionClicked -> {
                copySession(action.copyString)
                _state.update { it.copy(showSessionDialog = false) }
            }
        }
    }

    private fun copySession(copyString: String) {
        val s = state.value.currentSession
        val newName = "${s.name} - $copyString"
        viewModelScope.launch {
            val newSession = s.copy(
                id = null,
                name = newName,
                lastAccessMillis = System.currentTimeMillis()
            )
            val newId = appDao.upsertSession(newSession)
            launch {
                appDao.getPreSelectedTagsListForSession(s.id!!)
                    .map { it.copy(sessionId = newId,) }
                    .forEach { psl -> appDao.upsertPreSelectedTag(psl) }
            }
            launch {
                appDao.getPreSelectedPersonsListForSession(s.id!!)
                    .map { it.copy(sessionId = newId,) }
                    .forEach { psl -> appDao.upsertPreSelectedPerson(psl) }
            }
            launch {
                appDao.getPreSelectedPlacesListForSession(s.id!!)
                    .map { it.copy(sessionId = newId,) }
                    .forEach { psl -> appDao.upsertPreSelectedPlace(psl) }
            }
        }
    }

    companion object {
        private const val TAG = "SessionsScreenViewModel"
    }
}
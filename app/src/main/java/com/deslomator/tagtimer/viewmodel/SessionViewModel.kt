package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.SessionAction
import com.deslomator.tagtimer.dao.EventDao
import com.deslomator.tagtimer.dao.SessionDao
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.state.SessionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SessionViewModel(
    private val sessionDao: SessionDao,
    private val eventDao: EventDao
): ViewModel() {

    private val _state = MutableStateFlow(SessionState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SessionState())

    fun onAction(action: SessionAction) {
        when(action) {
            SessionAction.AddSession -> {
                _state.update { it.copy(isAddingSession = true) }
            }
            is SessionAction.DeleteSession -> {
                viewModelScope.launch { sessionDao.deleteSession(action.session) }
            }
            is SessionAction.EditSession -> {
                _state.update { it.copy(
                    editingIndex = action.editingIndex,
                    isEditingSession = true,
                    lastAccessMillis = System.currentTimeMillis(),
                    color = action.session.color,
                    name = action.session.name
                ) }
            }
            is SessionAction.UpsertSession -> {
                TODO()
            }
            is SessionAction.UpdateColor -> {
                _state.update { it.copy(color = action.color) }
            }
            is SessionAction.UpdateName -> {
                _state.update { it.copy(name = action.name) }
            }
            is SessionAction.SessionEdited -> {
                val session = Session(
                    name = _state.value.name,
                    color = _state.value.color,
                    lastAccessMillis = System.currentTimeMillis()
                )
                _state.update { it.copy(
                    isEditingSession = false,
                    color = 0,
                    name = ""
                ) }
                viewModelScope.launch { sessionDao.upsertSession(session) }
            }
            is SessionAction.SessionAdded -> {
                val session = Session(
                    name = _state.value.name,
                    color = _state.value.color,
                    lastAccessMillis = System.currentTimeMillis()
                )
                _state.update { it.copy(
                    isAddingSession = false,
                    color = 0,
                    name = ""
                ) }
                viewModelScope.launch { sessionDao.upsertSession(session) }
            }
            SessionAction.HideDeleteDialog -> {
                _state.update { it.copy(showDeleteDialog = false) }
            }
            SessionAction.ShowDeleteDialog -> {
                _state.update { it.copy(showDeleteDialog = true) }
            }
        }
    }
}
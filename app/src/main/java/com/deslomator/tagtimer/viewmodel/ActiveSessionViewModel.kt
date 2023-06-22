package com.deslomator.tagtimer.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.PreSelectedTag
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.state.ActiveSessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActiveSessionViewModel @Inject constructor(
    private val appDao: AppDao,
): ViewModel() {

    private val _sessionId = MutableStateFlow(0)
    private val _state = MutableStateFlow(ActiveSessionState())
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _events = _sessionId
        .flatMapLatest {
            Log.d(TAG, "_events, creating, session id: $it")
            appDao.getActiveEventsForSession(_sessionId.value)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _preSelectedTags = _sessionId
        .flatMapLatest {
            Log.d(TAG, "_preSelectedTags, creating, session id: $it")
            appDao.getPreSelectedTagsForSession(_sessionId.value)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _tags = appDao.getActiveTags()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val state = combine(_state, _events, _preSelectedTags, _tags) { state, events, preSelectedTags, tags ->
        state.copy(
            events = events,
            preSelectedTags = preSelectedTags,
            tags = tags,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ActiveSessionState())

    fun onAction(action: ActiveSessionAction) {
        when(action) {
            is ActiveSessionAction.UpdateSessionId -> {
                _sessionId.update { action.id }
                viewModelScope.launch {
                    _state.update {
                        it.copy(currentSession = appDao.getSession(action.id))
                    }
                    val session = Session(
                        lastAccessMillis = System.currentTimeMillis(),
                        name = state.value.currentSession.name,
                        color = state.value.currentSession.color,
                        startTimeMillis = state.value.currentSession.startTimeMillis,
                        endTimeMillis = state.value.currentSession.endTimeMillis,
                        id = state.value.currentSession.id,
                    )
                    appDao.upsertSession(session) }
            }
            is ActiveSessionAction.PlayPauseClicked -> {
                _state.update { it.copy(isRunning = !state.value.isRunning) }
            }
            is ActiveSessionAction.SelectTagsClicked -> {
                _state.update { it.copy( showTagsDialog = true) }
            }
            is ActiveSessionAction.DismissTagDialog -> {
                _state.update { it.copy( showTagsDialog = false) }
            }
            is ActiveSessionAction.SelectTagCheckedChange -> {
                if (action.checked) {
                    val pst = PreSelectedTag (
                        sessionId = state.value.currentSession.id,
                        tagId = action.tagId
                    )
                    viewModelScope.launch { appDao.upsertPreSelectedTag(pst) }
                } else {
                    val pst = state.value.preSelectedTags
                        .firstOrNull { it.tagId == action.tagId }
                    pst?.let { viewModelScope.launch { appDao.deletePreSelectedTag(it) } }
                }
            }
            ActiveSessionAction.AcceptTagSelectionClicked -> {
                _state.update { it.copy( showTagsDialog = false) }
            }
            is ActiveSessionAction.ActiveSessionClicked -> {

            }
            is ActiveSessionAction.StopSession -> {
                _state.update { it.copy( isRunning = false) }
            }
        }
    }

    companion object {
        private const val TAG = "ActiveSessionViewModel"
    }
}
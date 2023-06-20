package com.deslomator.tagtimer.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.Tag
import com.deslomator.tagtimer.state.ActiveSessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
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
    private val _events = appDao.getActiveEventsForSession(_sessionId.value)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _preSelectedTags = appDao.getPreSelectedTagsForSession(_sessionId.value)
        .map {
            it.map { r -> appDao.getTag(r.tagId) }
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
                Log.d(TAG, "updating session ID: ${action.id}")
                _sessionId.value = action.id
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

            }
            is ActiveSessionAction.AddTagClicked -> {

            }
        }
    }

    companion object {
        private const val TAG = "ActiveSessionViewModel"
    }
}
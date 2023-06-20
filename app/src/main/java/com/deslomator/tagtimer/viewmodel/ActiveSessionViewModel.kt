package com.deslomator.tagtimer.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.state.ActiveSessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ActiveSessionViewModel @Inject constructor(
    private val appDao: AppDao,
): ViewModel() {

    private val _sessionId = MutableStateFlow(0)
    private val _state = MutableStateFlow(ActiveSessionState())
    private val _events = appDao.getActiveEventsForSession(_sessionId.value)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _usedTags = appDao.getUsedTagsForSession(_sessionId.value)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _tags = appDao.getActiveTags()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val state = combine(_state, _events, _usedTags, _tags) { state, events, usedTags, tags ->
        state.copy(
            events = events,
            usedTags = usedTags,
            tags = tags,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ActiveSessionState())

    fun onAction(action: ActiveSessionAction) {
        when(action) {
            is ActiveSessionAction.UpdateSessionId -> {
                Log.d(TAG, "updating session ID: ${action.id}")
                _sessionId.value = action.id
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
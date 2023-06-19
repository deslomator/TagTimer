package com.deslomator.tagtimer.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.SessionsTrashAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.Tag
import com.deslomator.tagtimer.model.Trash
import com.deslomator.tagtimer.state.SessionsTrashState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionsTrashViewModel @Inject constructor(
    private val appDao: AppDao,
): ViewModel() {

    private val _state = MutableStateFlow(SessionsTrashState())
    private val _sessions = appDao.getTrashedSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _tags = appDao.getTrashedTags()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val state = combine(_state, _sessions, _tags) { state, sessions, tags ->
        state.copy(
            sessions = sessions,
            tags = tags,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SessionsTrashState())

    fun onAction(action: SessionsTrashAction) {
        when(action) {
            is SessionsTrashAction.ShowSessionsClicked -> {
                _state.update { it.copy(currentTrash = Trash.SESSION) }
            }
            is SessionsTrashAction.ShowTagsClicked -> {
                _state.update { it.copy(currentTrash = Trash.TAG) }
            }
            is SessionsTrashAction.DeleteSessionClicked -> {
                viewModelScope.launch {
                    appDao.deleteSession(action.session)
                    appDao.deleteEventsForSession(action.session.id)
                }
                _state.update { it.copy(
                    snackbarMessage = R.string.session_deleted,
                    showSnackBar = true
                ) }
            }
            is SessionsTrashAction.RestoreSessionClicked -> {
                viewModelScope.launch {
                    val trashed = Session(
                        lastAccessMillis = action.session.lastAccessMillis,
                        name = action.session.name,
                        color = action.session.color,
                        startTimeMillis = action.session.startTimeMillis,
                        endTimeMillis = action.session.endTimeMillis,
                        inTrash = false,
                        id = action.session.id,
                    )
                    appDao.upsertSession(trashed)
                }
                _state.update { it.copy(
                    snackbarMessage = R.string.session_restored,
                    showSnackBar = true
                ) }
            }
            is SessionsTrashAction.DeleteTagClicked -> {
                viewModelScope.launch {
                    appDao.deleteTag(action.tag)
                }
                _state.update { it.copy(
                    snackbarMessage = R.string.tag_deleted,
                    showSnackBar = true
                ) }
            }
            is SessionsTrashAction.RestoreTagClicked -> {
                viewModelScope.launch {
                    val trashed = Tag(
                        label = action.tag.label,
                        category = action.tag.category,
                        color = action.tag.color,
                        inTrash = false,
                        id = action.tag.id,
                    )
                    appDao.upsertTag(trashed)
                }
                _state.update { it.copy(
                    snackbarMessage = R.string.tag_restored,
                    showSnackBar = true
                ) }
            }
            is SessionsTrashAction.HideSnackbar -> {
//                Log.d(TAG, "HideSessionTrashSnackbar")
                _state.update { it.copy(showSnackBar = false) }
            }
        }
    }

    companion object {
        private const val TAG = "SessionsTrashViewModel"
    }
}
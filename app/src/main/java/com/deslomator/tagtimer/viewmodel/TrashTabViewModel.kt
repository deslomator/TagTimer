package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.TrashTabAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Trash
import com.deslomator.tagtimer.state.TrashTabState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrashTabViewModel @Inject constructor(
    private val appDao: AppDao,
): ViewModel() {

    private val _state = MutableStateFlow(TrashTabState())
    private val _sessions = appDao.getTrashedSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _tags = appDao.getTrashedTags()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val state = combine(_state, _sessions, _tags) { state, sessions, tags ->
        state.copy(
            sessions = sessions,
            tags = tags,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TrashTabState())

    fun onAction(action: TrashTabAction) {
        when(action) {
            is TrashTabAction.ShowSessionsClicked -> {
                _state.update { it.copy(currentTrash = Trash.SESSION) }
            }
            is TrashTabAction.ShowTagsClicked -> {
                _state.update { it.copy(currentTrash = Trash.TAG) }
            }
            is TrashTabAction.DeleteSessionClicked -> {
                viewModelScope.launch {
                    appDao.deleteSession(action.session)
                    appDao.deleteEventsForSession(action.session.id)
                }
                _state.update { it.copy(showSnackBar = false) }
                _state.update { it.copy(
                    snackbarMessage = R.string.session_deleted,
                    showSnackBar = true
                ) }
            }
            is TrashTabAction.RestoreSessionClicked -> {
                viewModelScope.launch {
                    val trashed = action.session.copy(inTrash = false)
                    appDao.upsertSession(trashed)
                }
                _state.update { it.copy(showSnackBar = false) }
                _state.update { it.copy(
                    snackbarMessage = R.string.session_restored,
                    showSnackBar = true
                ) }
            }
            is TrashTabAction.DeleteTagClicked -> {
                viewModelScope.launch {
                    appDao.deleteTag(action.tag)
                }
                _state.update { it.copy(showSnackBar = false) }
                _state.update { it.copy(
                    snackbarMessage = R.string.tag_deleted,
                    showSnackBar = true
                ) }
            }
            is TrashTabAction.RestoreTagClicked -> {
                viewModelScope.launch {
                    val trashed = action.tag.copy(inTrash = false)
                    appDao.upsertTag(trashed)
                }
                _state.update { it.copy(showSnackBar = false) }
                _state.update { it.copy(
                    snackbarMessage = R.string.tag_restored,
                    showSnackBar = true
                ) }
            }
            is TrashTabAction.HideSnackbar -> {
//                Log.d(TAG, "HideSessionTrashSnackbar")
                _state.update { it.copy(showSnackBar = false) }
            }
        }
    }

    companion object {
        private const val TAG = "SessionsTrashViewModel"
    }
}
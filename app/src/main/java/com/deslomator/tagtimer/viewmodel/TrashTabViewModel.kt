package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.TrashTabAction
import com.deslomator.tagtimer.dao.AppDao
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
    private val _persons = appDao.getTrashedPersons()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _places = appDao.getTrashedPlaces()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _tags = appDao.getTrashedTags()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val state = combine(_state, _sessions, _tags, _persons, _places) { state, sessions, tags, persons, places ->
        state.copy(
            sessions = sessions,
            tags = tags,
            persons = persons,
            places = places
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TrashTabState())

    fun onAction(action: TrashTabAction) {
        when(action) {
            is TrashTabAction.TrashTypeClicked -> {
                _state.update { it.copy(currentTrash = action.trash) }
            }
            is TrashTabAction.DeleteSessionClicked -> {
                viewModelScope.launch {
                    appDao.deleteSession(action.session)
                    appDao.deleteEventsForSession(action.session.id)
                }
            }
            is TrashTabAction.RestoreSessionClicked -> {
                viewModelScope.launch {
                    val trashed = action.session.copy(inTrash = false)
                    appDao.upsertSession(trashed)
                }
            }
            is TrashTabAction.DeleteTagClicked -> {
                viewModelScope.launch {
                    appDao.deleteTag(action.tag)
                }
            }
            is TrashTabAction.RestoreTagClicked -> {
                viewModelScope.launch {
                    val trashed = action.tag.copy(inTrash = false)
                    appDao.upsertTag(trashed)
                }
            }
            is TrashTabAction.DeletePersonClicked -> TODO()
            is TrashTabAction.DeletePlaceClicked -> TODO()
            is TrashTabAction.RestorePersonClicked -> TODO()
            is TrashTabAction.RestorePlaceClicked -> TODO()
        }
    }

    companion object {
        private const val TAG = "SessionsTrashViewModel"
    }
}
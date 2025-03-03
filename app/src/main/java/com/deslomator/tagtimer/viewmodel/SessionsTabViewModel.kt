package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.SessionsTabAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Preference
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.type.DialogState
import com.deslomator.tagtimer.model.type.PrefKey
import com.deslomator.tagtimer.model.type.SessionSort
import com.deslomator.tagtimer.populateDb
import com.deslomator.tagtimer.state.SessionsTabState
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
class SessionsTabViewModel @Inject constructor(
    private val appDao: AppDao,
) : ViewModel() {

    private val _sort = SortProvider.getSessionSort(appDao, viewModelScope)

    private val _state = MutableStateFlow(SessionsTabState())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _sessions = _sort.flatMapLatest { sSort ->
//        Log.d(TAG, "choosing sorted session list")
        when (sSort) {
            SessionSort.DATE -> appDao.getSessionsByDate()
            SessionSort.LAST_ACCESS -> appDao.getSessionsByLastAccess()
            else -> appDao.getSessionsByName()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val state = combine(_state, _sort, _sessions) { state, sort, sessions ->
        state.copy(
            sessions = sessions,
            sessionSort = sort
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SessionsTabState())

    fun onAction(action: SessionsTabAction) {
        when (action) {
            SessionsTabAction.AddNewSessionClicked -> {
                _state.update {
                    it.copy(
                        currentSession = Session(),
                        sessionDialogState = DialogState.NEW_ITEM,
                    )
                }
            }

            is SessionsTabAction.ItemClicked -> {
                _state.update {
                    it.copy(
                        currentSession = action.session,
                        sessionDialogState = DialogState.EDIT_CAN_DELETE
                    )
                }
            }

            is SessionsTabAction.DialogAcceptClicked -> {
                _state.update { it.copy(sessionDialogState = DialogState.HIDDEN) }
                viewModelScope.launch { appDao.upsertSession(action.session) }
            }

            is SessionsTabAction.DismissSessionDialog -> {
                _state.update { it.copy(sessionDialogState = DialogState.HIDDEN) }
            }

            is SessionsTabAction.TrashSessionClicked -> {
                viewModelScope.launch {
                    _state.update { it.copy(sessionDialogState = DialogState.HIDDEN) }
                    val trashed = state.value.currentSession.copy(
                        running = false,
                        inTrash = true
                    )
                    appDao.upsertSession(trashed)
                }
            }

            is SessionsTabAction.PopulateDbClicked -> {
                viewModelScope.launch { populateDb(appDao) }
            }

            is SessionsTabAction.CopySessionClicked -> {
                copySession(action.copyString)
                _state.update { it.copy(sessionDialogState = DialogState.HIDDEN) }
            }

            is SessionsTabAction.SessionSortClicked -> {
                val pref = Preference(
                    prefKey = PrefKey.SESSION_SORT.name, value = action.sessionSort.name
                )
                viewModelScope.launch { appDao.upsertPreference(pref) }
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
                eventCount = 0,
                durationMillis = 0,
                startTimestampMillis = -1,
                running = false,
                lastAccessMillis = System.currentTimeMillis()
            )
            val newId = appDao.upsertSession(newSession)
            launch {
                appDao.getPreSelectedListForSession(s.id!!)
                    .map{
                        it.copy(sessionId = newId)
                    }
                    .let { appDao.upsertPreSelectedLabels(it) }
            }
        }
    }


    companion object {
        private const val TAG = "SessionsScreenViewModel"
    }
}
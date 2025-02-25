package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.SessionsTabAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Preference
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.type.PrefKey
import com.deslomator.tagtimer.model.type.SessionSort
import com.deslomator.tagtimer.populateDb
import com.deslomator.tagtimer.state.SessionsTabState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionsTabViewModel @Inject constructor(
    private val appDao: AppDao,
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _sort = appDao.getPreferences().mapLatest { prefsList ->
        prefsList.firstOrNull { item ->
            item.key == PrefKey.SESSION_SORT.name
        }?.getSessionSort() ?: SessionSort.LAST_ACCESS
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), SessionSort.LAST_ACCESS)

    private val _state = MutableStateFlow(SessionsTabState())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _sessions = _sort.flatMapLatest { sSort ->
            when (sSort) {
                SessionSort.DATE -> appDao.getSessionsByDate()
                SessionSort.LAST_ACCESS -> appDao.getSessionsByLastAccess()
                SessionSort.NAME -> appDao.getSessionsByName()
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val state = combine(_sort, _state, _sessions) { sort, state, sessions ->
        state.copy(
            sessions = sessions, sessionSort = sort
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SessionsTabState())

    fun onAction(action: SessionsTabAction) {
        when (action) {
            SessionsTabAction.AddNewSessionClicked -> {
                _state.update {
                    it.copy(
                        currentSession = Session(),
                        isEditingSession = false,
                        showSessionDialog = true,
                    )
                }
            }

            is SessionsTabAction.ItemClicked -> {
                _state.update {
                    it.copy(
                        currentSession = action.session,
                        isEditingSession = true,
                        showSessionDialog = true
                    )
                }
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
                    val trashed = state.value.currentSession.copy(
                        running = false, inTrash = true
                    )
                    appDao.upsertSession(trashed)
                }
            }

            is SessionsTabAction.PopulateDbClicked -> {
                viewModelScope.launch { populateDb(appDao) }
            }

            is SessionsTabAction.CopySessionClicked -> {
                copySession(action.copyString)
                _state.update { it.copy(showSessionDialog = false) }
            }

            is SessionsTabAction.SessionSortClicked -> {
//                Log.d(TAG, "session sort: ${action.sessionSort.name}")
                val pref = Preference(
                    key = PrefKey.SESSION_SORT.name, value = action.sessionSort.name
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
                appDao.getPreSelectedTagsListForSession(s.id!!).map { it.copy(sessionId = newId) }
                    .forEach { psl -> appDao.upsertPreSelectedTag(psl) }
            }
            launch {
                appDao.getPreSelectedPersonsListForSession(s.id!!)
                    .map { it.copy(sessionId = newId) }
                    .forEach { psl -> appDao.upsertPreSelectedPerson(psl) }
            }
            launch {
                appDao.getPreSelectedPlacesListForSession(s.id!!).map { it.copy(sessionId = newId) }
                    .forEach { psl -> appDao.upsertPreSelectedPlace(psl) }
            }
        }
    }

    /**
     * only gets called for running sessions, no need to check
     */
    private fun getSessionDuration(s: Session): Long {
        return System.currentTimeMillis() - s.startTimestampMillis
    }

    private suspend fun updateDurations() {
        while (true) {
            val sessions = state.value.sessions
            sessions.filter { it.running }.forEach { runningSession ->
                val updated = runningSession.copy(
                    durationMillis = getSessionDuration(runningSession)
                )
                appDao.upsertSession(updated)
            }
            delay(1000)
        }
    }

    init {
        viewModelScope.launch {
            updateDurations()
        }
    }

    companion object {
        private const val TAG = "SessionsScreenViewModel"
    }
}
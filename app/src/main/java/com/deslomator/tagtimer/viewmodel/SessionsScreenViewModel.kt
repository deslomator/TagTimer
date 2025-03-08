package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.SessionsTabAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Preference
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.type.DialogState
import com.deslomator.tagtimer.model.type.ItemState
import com.deslomator.tagtimer.model.type.PrefKey
import com.deslomator.tagtimer.model.ancillary.PreferenceProvider
import com.deslomator.tagtimer.model.type.SessionSort
import com.deslomator.tagtimer.populateDb
import com.deslomator.tagtimer.state.SessionsScreenState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SessionsScreenViewModel(
    private val appDao: AppDao,
) : ViewModel() {

    private val _state = MutableStateFlow(SessionsScreenState())

    private val _prefs = appDao.getPreferences()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _prefProvider = _prefs.mapLatest { prefs ->
        PreferenceProvider(prefs)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PreferenceProvider())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _sessions = _prefProvider.flatMapLatest { prefProvider ->
//        Log.d(TAG, "choosing sorted session list")
        appDao.getSessions().map { sessions ->
            val enabled = if (prefProvider.showEnabledSessions()) sessions.filter { it.state == ItemState.ENABLED } else emptyList()
            val archived = if (prefProvider.showArchivedSessions()) sessions.filter { it.state == ItemState.ARCHIVED } else emptyList()
            val trashed = if (prefProvider.showTrashedSessions()) sessions.filter { it.state == ItemState.TRASHED } else emptyList()
            (enabled + archived + trashed).run {
                when (prefProvider.sessionSort()) {
                    SessionSort.NAME -> sortedBy { it.name }
                    SessionSort.DATE -> sortedByDescending { it.sessionDateMillis }
                    else -> sortedByDescending { it.lastAccessMillis }
                }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    val state = combine(_state, _sessions, _prefProvider) { state, sessions, prefProvider ->
        state.copy(
            sessions = sessions,
            preferenceProvider = prefProvider
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SessionsScreenState())


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

            is SessionsTabAction.EditSessionClicked -> {
                val dialogState = when (action.session.state) {
                    ItemState.ENABLED -> DialogState.ENABLED
                    ItemState.ARCHIVED -> DialogState.ARCHIVED
                    ItemState.TRASHED -> DialogState.TRASHED
                }
                _state.update {
                    it.copy(
                        currentSession = action.session,
                        sessionDialogState = dialogState
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

            is SessionsTabAction.ArchiveSessionClicked -> {
                viewModelScope.launch {
                    _state.update { it.copy(sessionDialogState = DialogState.HIDDEN) }
                    val archived = state.value.currentSession.copy(
                        running = false,
                        state = ItemState.ARCHIVED
                    )
                    appDao.upsertSession(archived)
                }
            }

            is SessionsTabAction.UnArchiveSessionClicked -> {
                viewModelScope.launch {
                    _state.update { it.copy(sessionDialogState = DialogState.HIDDEN) }
                    val unarchived = state.value.currentSession.copy(
                        running = false,
                        state = ItemState.ENABLED
                    )
                    appDao.upsertSession(unarchived)
                }
            }

            is SessionsTabAction.TrashSessionClicked -> {
                viewModelScope.launch {
                    _state.update { it.copy(sessionDialogState = DialogState.HIDDEN) }
                    val trashed = state.value.currentSession.copy(
                        running = false,
                        state = ItemState.TRASHED
                    )
                    appDao.upsertSession(trashed)
                }
            }

            is SessionsTabAction.UnTrashSessionClicked -> {
                viewModelScope.launch {
                    _state.update { it.copy(sessionDialogState = DialogState.HIDDEN) }
                    val untrashed = state.value.currentSession.copy(
                        running = false,
                        state = ItemState.ENABLED
                    )
                    appDao.upsertSession(untrashed)
                }
            }

            is SessionsTabAction.PurgeSessionClicked -> {
                viewModelScope.launch {
                    _state.update { it.copy(sessionDialogState = DialogState.HIDDEN) }
                    appDao.deleteSession(state.value.currentSession)
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
                    prefKey = PrefKey.SESSION_SORT,
                    value = action.sessionSort.name
                )
                viewModelScope.launch { appDao.upsertPreference(pref) }
            }

            is SessionsTabAction.ShowEnabledClicked -> {
                val pref = Preference(
                    prefKey = PrefKey.SHOW_ENABLED_SESSIONS,
                    value = action.show.toString()
                )
                viewModelScope.launch { appDao.upsertPreference(pref) }
            }

            is SessionsTabAction.ShowArchivedClicked -> {
                val pref = Preference(
                    prefKey = PrefKey.SHOW_ARCHIVED_SESSIONS,
                    value = action.show.toString()
                )
                viewModelScope.launch { appDao.upsertPreference(pref) }
            }

            is SessionsTabAction.ShowTrashedClicked -> {
                val pref = Preference(
                    prefKey = PrefKey.SHOW_TRASHED_SESSIONS, value =
                        action.show.toString()
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
                appDao.getSelectedLabelsListForSession(s.id!!)
                    .map{
                        it.copy(sessionId = newId)
                    }
                    .let { appDao.upsertSelected(it) }
            }
        }
    }

    companion object {
        private const val TAG = "SessionsScreenViewModel"
    }
}
package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.SharedAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Preference
import com.deslomator.tagtimer.model.type.PrefKey
import com.deslomator.tagtimer.model.type.Sort
import com.deslomator.tagtimer.state.SharedState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val appDao: AppDao,
) : ViewModel() {

    private var timerJob: Job? = null

    private val _prefs = appDao.getPreferences()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(SharedState())
    val state = combine(_state, _prefs) { state, prefs ->
        state.copy(
            tagSort = prefs.firstOrNull { it.sKey == PrefKey.TAG_SORT.name }?.getSort() ?: Sort.COLOR,
            personSort = prefs.firstOrNull { it.sKey == PrefKey.PERSON_SORT.name }?.getSort() ?: Sort.NAME,
            placeSort = prefs.firstOrNull { it.sKey == PrefKey.PLACE_SORT.name }?.getSort() ?: Sort.NAME,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SharedState()
    )

    fun onAction(action: SharedAction) {
        when(action) {
            is SharedAction.TagSortClicked -> {
                val pref = Preference(
                    value = action.tagSort.name,
                    sKey = PrefKey.TAG_SORT.name
                )
                viewModelScope.launch { appDao.upsertPreference(pref) }
            }
            is SharedAction.PersonSortClicked -> {
                val pref = Preference(
                    value = action.personSort.name,
                    sKey = PrefKey.PERSON_SORT.name
                )
                viewModelScope.launch { appDao.upsertPreference(pref) }
            }
            is SharedAction.PlaceSortClicked -> {
                val pref = Preference(
                    value = action.placeSort.name,
                    sKey = PrefKey.PLACE_SORT.name
                )
                viewModelScope.launch { appDao.upsertPreference(pref) }
            }
            is SharedAction.SetCursor -> {
                _state.update { it.copy(cursor = action.cursor) }
            }
            is SharedAction.PlayPauseClicked -> {
                timerJob = if (state.value.isRunning) {
                    timerJob?.cancel()
                    null
                } else viewModelScope.launch(Dispatchers.Main) {
                    while (true) {
                        delay(1000)
                        _state.update { it.copy(cursor = state.value.cursor + 1000) }
                    }
                }
                _state.update { it.copy(isRunning = !state.value.isRunning) }
            }
            SharedAction.StopTimer -> {
                _state.update { it.copy(isRunning = false) }
                with(timerJob) {
                    this?.cancel()
                    timerJob = null
                }
            }
        }
    }

    companion object {
        private const val TAG = "SharedViewModel"
    }
}
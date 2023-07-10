package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.SharedAction
import com.deslomator.tagtimer.state.SharedState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {

    private var timerJob: Job? = null

    private val _state = MutableStateFlow(SharedState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SharedState()
    )

    fun onAction(action: SharedAction) {
        when(action) {
            is SharedAction.TagSortClicked -> {
                _state.update { it.copy(tagSort = action.tagSort) }
            }
            is SharedAction.PersonSortClicked -> {
                _state.update { it.copy(personSort = action.personSort) }
            }
            is SharedAction.PlaceSortClicked -> {
                _state.update { it.copy(placeSort = action.placeSort) }
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
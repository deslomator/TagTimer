package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.AppAction
import com.deslomator.tagtimer.state.AppState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class AppViewModel: ViewModel() {

    private val _state = MutableStateFlow(AppState())
    val state = _state
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppState())

    fun onAction(action: AppAction) {
        when(action) {
            is AppAction.activateScreen -> {
                _state.update { it.copy(currentScreen = action.screen) }
            }
        }
    }

    companion object {
        private const val TAG = "AppViewModel"
    }
}
package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.ui.Screen

sealed interface AppAction {
    data class activateScreen(val screen: Screen): AppAction
}
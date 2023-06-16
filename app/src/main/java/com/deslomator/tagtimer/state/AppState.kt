package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.ui.Screen

data class AppState(
    val currentScreen: Screen = Screen.SESSIONS,
)

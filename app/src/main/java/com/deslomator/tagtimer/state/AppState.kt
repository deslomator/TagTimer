package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.ui.Screen
import androidx.compose.runtime.saveable.Saver

data class AppState(
    val currentScreen: Screen = Screen.SESSIONS,
) {
    companion object {
        val saver = Saver<AppState, String>(
            save = { it.currentScreen.name },
            restore = { AppState(Screen.valueOf(it)) }
        )
    }
}

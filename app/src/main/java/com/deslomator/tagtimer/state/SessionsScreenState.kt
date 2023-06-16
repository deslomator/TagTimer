package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.ui.Screen

data class SessionsScreenState(
    val activeScreen: Screen = Screen.SESSIONS,
    val sessions: List<Session> = emptyList(),
    val currentSession: Session = Session(),
    val lastAccessMillis: Long = 0,
    val sessionName: String = "",
    val sessionColor: Int = 0,
    val showSessionDialog: Boolean = false,
    val isEditingSession: Boolean = false,
    val isAddingNewSession: Boolean = false,
    val showSessionDeleteSnackbar: Boolean = false,
)

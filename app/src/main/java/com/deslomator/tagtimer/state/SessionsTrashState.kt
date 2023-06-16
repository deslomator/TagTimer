package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.ui.Screen

data class SessionsTrashState(
    val activeScreen: Screen = Screen.SESSIONS_TRASH,
    val sessions: List<Session> = emptyList(),
    val currentSession: Session = Session(),
)

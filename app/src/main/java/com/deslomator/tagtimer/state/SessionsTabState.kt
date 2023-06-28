package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Session

data class SessionsTabState(
    val sessions: List<Session> = emptyList(),
    val currentSession: Session = Session(),
    val lastAccessMillis: Long = 0,
    val showSessionDialog: Boolean = false,
)

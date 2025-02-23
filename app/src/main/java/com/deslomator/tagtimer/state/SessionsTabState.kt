package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Session

data class SessionsTabState(
    val sessions: List<Session> = emptyList(),
    val currentSession: Session = Session(),
    val showSessionDialog: Boolean = false,
    val isEditingSession: Boolean = false,
)

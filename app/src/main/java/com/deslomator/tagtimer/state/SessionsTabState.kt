package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.type.SessionSort

data class SessionsTabState(
    val sessions: List<Session> = emptyList(),
    val sessionSort: SessionSort = SessionSort.LAST_ACCESS,
    val currentSession: Session = Session(),
    val showSessionDialog: Boolean = false,
    val isEditingSession: Boolean = false,
)

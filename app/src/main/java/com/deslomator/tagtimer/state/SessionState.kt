package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Session

data class SessionState(
    val sessions: List<Session> = emptyList(),
    val lastAccess: Long,
    val name: String,
    val color: Long,
    val startTimeMillis: Long,
    val endTimeMillis: Long,
    val isAddingSession: Boolean = false
)

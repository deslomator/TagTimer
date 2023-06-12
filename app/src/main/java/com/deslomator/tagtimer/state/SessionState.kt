package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Session

data class SessionState(
    val sessions: List<Session> = emptyList(),
    val editingIndex: Int = 0,
    val lastAccessMillis: Long = 0,
    val name: String = "",
    val color: Long = 0,
    val isAddingSession: Boolean = false,
    val isEditingSession: Boolean = false,
    val showDeleteDialog: Boolean = false
)

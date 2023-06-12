package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Event

data class EventState(
    val events: List<Event> = emptyList(),
    val sessionId: Long,
    val timestamp: Long,
    val group: String,
    val label: String,
    val color: Long,
    val notes: String,
    val isEditingNotes: Boolean = false
)

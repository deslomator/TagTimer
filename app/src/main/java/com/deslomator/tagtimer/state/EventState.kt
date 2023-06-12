package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Event

data class EventState(
    val events: List<Event> = emptyList(),
    val editingIndex: Int = 0,
    val note: String = "",
    val isEditingNote: Boolean = false,
    val showDeleteDialog: Boolean = false
)

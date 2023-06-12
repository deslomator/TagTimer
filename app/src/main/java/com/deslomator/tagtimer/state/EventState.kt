package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Event

data class EventState(
    val events: List<Event> = emptyList(),
    val note: String = "",
    val isEditingNote: Boolean = false
)

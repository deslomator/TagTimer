package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.EventForDisplay
import com.deslomator.tagtimer.model.Session

data class EventTrashState(
    val trashedEvents: List<EventForDisplay> = emptyList(),
    val currentSession: Session = Session(),
    val showSnackbar: Boolean = false,
    val eventForDialog: EventForDisplay = EventForDisplay(),
    val showEventInTrashDialog: Boolean = false,
)

package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.ancillary.EventForDisplay

data class TrashTabState(
    val currentSession: Session = Session(),
    val showSnackbar: Boolean = false,
    val eventForDialog: EventForDisplay = EventForDisplay(),
    val showEventInTrashDialog: Boolean = false,
    val trashedEvents: List<EventForDisplay> = emptyList(),
)

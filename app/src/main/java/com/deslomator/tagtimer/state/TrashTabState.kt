package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.ancillary.EventForDisplay
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Session

data class TrashTabState(
    val currentSession: Session = Session(),
    val showSnackbar: Boolean = false,
    val eventForDialog: EventForDisplay = EventForDisplay(),
    val showEventInTrashDialog: Boolean = false,
    val trashedEvents: List<EventForDisplay> = emptyList(),
    val sessions: List<Session> = emptyList(),
    val tags: List<Label> = emptyList(),
    val persons: List<Label> = emptyList(),
    val places: List<Label> = emptyList(),
)

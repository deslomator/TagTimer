package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Session

data class EventTrashState(
    val trashedEvents: List<Event> = emptyList(),
    val currentSession: Session = Session(),
    val showSnackbar: Boolean = false,
    val eventForDialog: Event = Event(),
    val showEventInTrashDialog: Boolean = false,
)

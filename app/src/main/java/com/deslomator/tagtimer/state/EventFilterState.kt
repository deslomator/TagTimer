package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.EventForDisplay
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Session

data class EventFilterState(
    val events: List<EventForDisplay> = emptyList(),
    val eventsForDisplay: List<EventForDisplay> = emptyList(),
    val tags: List<Label> = emptyList(),
    val persons: List<Label> = emptyList(),
    val places: List<Label> = emptyList(),
    val currentSession: Session = Session(),
    val eventForDialog: EventForDisplay = EventForDisplay(),
    val eventForScrollTo: EventForDisplay = EventForDisplay(),
    val showEventEditionDialog: Boolean = false,
    val exportEvents: Boolean = false,
    val dataToExport: String = "",
    val query: String = "",
    val currentPerson: Label = Label(),
    val currentPlace: Label = Label(),
    val currentTags: List<Label> = emptyList(),
)

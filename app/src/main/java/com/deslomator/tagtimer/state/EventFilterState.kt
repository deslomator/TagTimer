package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.EventForDisplay
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Session

data class EventFilterState(
    val events: List<Event> = emptyList(),
    val eventsForDisplay: List<EventForDisplay> = emptyList(),
    val tags: List<Label> = emptyList(),
    val persons: List<Label> = emptyList(),
    val places: List<Label> = emptyList(),
    val currentSession: Session = Session(),
    val eventForDialog: EventForDisplay = EventForDisplay(),
    val eventForScrollTo: Event = Event(),
    val showEventEditionDialog: Boolean = false,
    val exportEvents: Boolean = false,
    val dataToExport: String = "",
    val query: String = "",
    val currentPerson: Long? = null,
    val currentPlace: Long? = null,
    val currentTags: List<Long> = emptyList(),
)

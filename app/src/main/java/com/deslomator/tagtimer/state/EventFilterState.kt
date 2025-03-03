package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Session

data class EventFilterState(
    val events: List<Event> = emptyList(),
    val tags: List<Label.Tag> = emptyList(),
    val persons: List<Label.Person> = emptyList(),
    val places: List<Label.Place> = emptyList(),
    val currentSession: Session = Session(),
    val eventForDialog: Event = Event(),
    val eventForScrollTo: Event = Event(),
    val showEventEditionDialog: Boolean = false,
    val exportEvents: Boolean = false,
    val dataToExport: String = "",
    val query: String = "",
    val currentPerson: String = "",
    val currentPlace: String = "",
    val currentTags: List<String> = emptyList(),
)

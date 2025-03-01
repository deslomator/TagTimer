package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.EventForDisplay
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Preselected
import com.deslomator.tagtimer.model.Session

data class ActiveSessionState(
    val events: List<Event> = emptyList(),
    val eventsForDisplay: List<EventForDisplay> = emptyList(),
    val preSelectedPersons: List<Label> = emptyList(),
    val preSelectedPlaces: List<Label> = emptyList(),
    val preSelectedTags: List<Label> = emptyList(),
    val tags: List<Label> = emptyList(),
    val persons: List<Label> = emptyList(),
    val places: List<Label> = emptyList(),
    val currentSession: Session = Session(),
    val showSnackbar: Boolean = false,
    val eventForDialog: EventForDisplay = EventForDisplay(),
    val eventForScrollTo: EventForDisplay = EventForDisplay(),
    val showEventEditionDialog: Boolean = false,
    val showTimeDialog: Boolean = false,
    val shareData: Boolean = false,
    val dataToShare: String = "",
    val currentPersonId: Long? = null,
    val currentPlaceId: Long? = null,
    val currentLabelId: Long? = null,
)

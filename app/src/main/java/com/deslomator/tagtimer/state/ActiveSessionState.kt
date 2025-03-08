package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.ancillary.EventForDisplay
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Session

data class ActiveSessionState(
    val eventsForDisplay: List<EventForDisplay> = emptyList(),
    val selectedPersons: List<Label> = emptyList(),
    val selectedPlaces: List<Label> = emptyList(),
    val selectedTags: List<Label> = emptyList(),
    val currentSession: Session = Session(),
    val showSnackbar: Boolean = false,
    val eventForDialog: EventForDisplay = EventForDisplay(),
    val eventForScrollTo: EventForDisplay = EventForDisplay(),
    val showEventEditionDialog: Boolean = false,
    val showTimeDialog: Boolean = false,
    val shareData: Boolean = false,
    val dataToShare: String = "",
    val currentPerson: Label? = null,
    val currentPlace: Label? = null,
    val currentLabel: Label? = null
)

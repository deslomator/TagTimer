package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.ancillary.EventForDisplay
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Session

data class ActiveSessionState(
    val eventsForDisplay: List<EventForDisplay> = emptyList(),
    val selectedTags: List<Label> = emptyList(),
    val selectedPersons: List<Label> = emptyList(),
    val selectedPlaces: List<Label> = emptyList(),
    val activeTags: List<Label> = emptyList(),
    val activePersons: List<Label> = emptyList(),
    val activePlaces: List<Label> = emptyList(),
    val currentSession: Session = Session(),
    val showSnackbar: Boolean = false,
    val eventForDialog: EventForDisplay = EventForDisplay(),
    val indexForScrollTo: Int? = null,
    val showEventEditionDialog: Boolean = false,
    val showTimeDialog: Boolean = false,
    val currentPerson: Label? = null,
    val currentPlace: Label? = null,
)

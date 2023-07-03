package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Preselected
import com.deslomator.tagtimer.model.Session

data class ActiveSessionState(
    val events: List<Event> = emptyList(),
    val preSelectedPersons: List<Preselected.Person> = emptyList(),
    val preSelectedPlaces: List<Preselected.Place> = emptyList(),
    val preSelectedTags: List<Preselected.Tag> = emptyList(),
    val tags: List<Label.Tag> = emptyList(),
    val persons: List<Label.Person> = emptyList(),
    val places: List<Label.Place> = emptyList(),
    val currentSession: Session = Session(),
    val isRunning: Boolean = false,
    val cursor: Long = 0,
    val showSnackbar: Boolean = false,
    val eventForDialog: Event = Event(),
    val eventForScrollTo: Event = Event(),
    val showEventEditionDialog: Boolean = false,
    val showTimeDialog: Boolean = false,
    val exportData: Boolean = false,
    val dataToExport: String = "",
    val currentPersonName: String = "",
    val currentPlaceName: String = "",
    val currentLabelName: String = "",
)

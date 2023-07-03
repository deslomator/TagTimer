package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Preselected
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.type.LabelScreen

data class EventFilterState(
    val events: List<Event> = emptyList(),
    val trashedEvents: List<Event> = emptyList(),
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
    val showEventInTrashDialog: Boolean = false,
    val showTimeDialog: Boolean = false,
    val exportEvents: Boolean = false,
    val dataToExport: String = "",
    val currentLabel: LabelScreen = LabelScreen.Tag,
    val currentPersonName: String = "",
    val currentPlaceName: String = "",
    val currentLabelName: String = "",
)

package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Lbl
import com.deslomator.tagtimer.model.PreSelectedPerson
import com.deslomator.tagtimer.model.PreSelectedPlace
import com.deslomator.tagtimer.model.PreSelectedTag
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.type.Label

data class ActiveSessionState(
    val events: List<Event> = emptyList(),
    val trashedEvents: List<Event> = emptyList(),
    val preSelectedTags: List<PreSelectedTag> = emptyList(),
    val preSelectedPersons: List<PreSelectedPerson> = emptyList(),
    val preSelectedPlaces: List<PreSelectedPlace> = emptyList(),
    val tags: List<Lbl.Tag> = emptyList(),
    val persons: List<Lbl.Person> = emptyList(),
    val places: List<Lbl.Place> = emptyList(),
    val currentSession: Session = Session(),
    val isRunning: Boolean = false,
    val cursor: Long = 0,
    val showSnackbar: Boolean = false,
    val eventForDialog: Event = Event(),
    val eventForScrollTo: Event = Event(),
    val showEventEditionDialog: Boolean = false,
    val showEventInTrashDialog: Boolean = false,
    val showTimeDialog: Boolean = false,
    val exportData: Boolean = false,
    val dataToExport: String = "",
    val currentLabel: Label = Label.Tag,
    val currentPersonName: String = "",
    val currentPlaceName: String = "",
    val currentLabelName: String = "",
)

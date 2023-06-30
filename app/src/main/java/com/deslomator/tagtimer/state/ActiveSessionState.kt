package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.type.Label
import com.deslomator.tagtimer.model.Person
import com.deslomator.tagtimer.model.Place
import com.deslomator.tagtimer.model.PreSelectedPerson
import com.deslomator.tagtimer.model.PreSelectedPlace
import com.deslomator.tagtimer.model.PreSelectedTag
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.Tag

data class ActiveSessionState(
    val events: List<Event> = emptyList(),
    val trashedEvents: List<Event> = emptyList(),
    val preSelectedTags: List<PreSelectedTag> = emptyList(),
    val preSelectedPersons: List<PreSelectedPerson> = emptyList(),
    val preSelectedPlaces: List<PreSelectedPlace> = emptyList(),
    val tags: List<Tag> = emptyList(),
    val persons: List<Person> = emptyList(),
    val places: List<Place> = emptyList(),
    val currentSession: Session = Session(),
    val showTagsDialog: Boolean = false,
    val isRunning: Boolean = false,
    val cursor: Long = 0,
    val showSnackbar: Boolean = false,
    val showEventTrash: Boolean = false,
    val showSessionEditionDialog: Boolean = false,
    val currentEvent: Event = Event(),
    val showEventEditionDialog: Boolean = false,
    val showEventInTrashDialog: Boolean = false,
    val showTimeDialog: Boolean = false,
    val exportSession: Boolean = false,
    val sessionToExport: String = "",
    val currentLabel: Label = Label.TAG,
    val currentPersonName: String = "",
    val currentPlaceName: String = "",
)

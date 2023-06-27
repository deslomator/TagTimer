package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.ExportedSession
import com.deslomator.tagtimer.model.PreSelectedTag
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.Tag

data class ActiveSessionState(
    val events: List<Event> = emptyList(),
    val trashedEvents: List<Event> = emptyList(),
    val preSelectedTags: List<PreSelectedTag> = emptyList(),
    val tags: List<Tag> = emptyList(),
    val currentSession: Session = Session(),
    val showTagsDialog: Boolean = false,
    val isRunning: Boolean = false,
    val baseTimeMillis: Long = 0,
    val showSnackbar: Boolean = false,
    val showEventTrash: Boolean = false,
    val showSessionEditionDialog: Boolean = false,
    val currentEvent: Event = Event(),
    val showEventEditionDialog: Boolean = false,
    val showEventInTrashDialog: Boolean = false,
    val exportSession: Boolean = false,
    val sessionToExport: String = ""
)

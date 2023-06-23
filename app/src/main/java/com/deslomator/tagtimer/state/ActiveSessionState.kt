package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.PreSelectedTag
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.Tag

data class ActiveSessionState(
    val events: List<Event> = emptyList(),
    val trashedEvents: List<Event> = emptyList(),
    val preSelectedTags: List<PreSelectedTag> = emptyList(),
    val tags: List<Tag> = emptyList(),
    val currentSession: Session = Session(),
    val startTimeMillis: Long = 0,
    val endTimeMillis: Long = 0,
    val offsetMillis: Long = 0,
    val sessionName: String = "",
    val sessionColor: Int = 0,
    val showTagsDialog: Boolean = false,
    val isRunning: Boolean = false,
    val isAbsoluteTime: Boolean = false,
    val showSnackbar: Boolean = false,
    val showEventTrash: Boolean = false,
    val showSessionEditionDialog: Boolean = false,
)

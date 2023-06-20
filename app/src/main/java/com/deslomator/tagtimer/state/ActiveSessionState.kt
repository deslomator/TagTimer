package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.Tag
import com.deslomator.tagtimer.model.UsedTag

data class ActiveSessionState(
    val events: List<Event> = emptyList(),
    val usedTags: List<UsedTag> = emptyList(),
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
)

package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.Tag

data class UiState(
    val events: List<Event> = emptyList(),
    val eventEditingIndex: Int = 0,
    val eventNote: String = "",
    val isEditingEventNote: Boolean = false,
    val showEventDeleteDialog: Boolean = false,
    val sessions: List<Session> = emptyList(),
    val sessionEditingIndex: Int = 0,
    val lastAccessMillis: Long = 0,
    val sessionName: String = "",
    val sessionColor: Long = 0,
    val isEditingSession: Boolean = false,
    val showSessionDeleteDialog: Boolean = false,
    val tags: List<Tag> = emptyList(),
    val tagEditingIndex: Int = 0,
    val tagCategory: String = "",
    val tagLabel: String = "",
    val tagColor: Long = 0,
    val isEditingTag: Boolean = false,
    val showDeleteTagDialog: Boolean = false,
    val showDeleteUsedTagDialog: Boolean = false
)

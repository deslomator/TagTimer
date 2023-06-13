package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.Tag
import com.deslomator.tagtimer.model.UsedTag

data class AppState(
    val events: List<Event> = emptyList(),
    val currentEvent: Event = Event(),
    val eventNote: String = "",
    val isEditingEventNote: Boolean = false,
    val showEventDeleteDialog: Boolean = false,

    val sessions: List<Session> = emptyList(),
    val currentSession: Session = Session(),
    val lastAccessMillis: Long = 0,
    val sessionName: String = "",
    val sessionColor: Int = 0,
    val showSessionDialog: Boolean = false,
    val isEditingSession: Boolean = false,
    val isAddingNewSession: Boolean = false,
    val showSessionDeleteDialog: Boolean = false,

    val tags: List<Tag> = emptyList(),
    val currentTag: Tag = Tag(),
    val tagCategory: String = "",
    val tagLabel: String = "",
    val tagColor: Int = 0,
    val isEditingTag: Boolean = false,
    val showDeleteTagDialog: Boolean = false,

    val usedTags: List<UsedTag> = emptyList(),
    val currentUsedTag: UsedTag = UsedTag(),
    val isEditingUsedTag: Boolean = false,
    val showDeleteUsedTagDialog: Boolean = false
)

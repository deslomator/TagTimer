package com.deslomator.tagtimer.state

import androidx.annotation.StringRes
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.Tag
import com.deslomator.tagtimer.model.Trash

data class TrashTabState(
    val currentTrash: Trash = Trash.SESSION,
    val sessions: List<Session> = emptyList(),
    val tags: List<Tag> = emptyList(),
    val currentSession: Session = Session(),
)

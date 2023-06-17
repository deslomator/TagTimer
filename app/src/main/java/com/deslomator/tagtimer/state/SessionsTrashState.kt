package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.model.Session

data class SessionsTrashState(
    val sessions: List<Session> = emptyList(),
    val currentSession: Session = Session(),
    val showSnackBar: Boolean = false,
    val snackbarMessage: Int = R.string.session_sent_to_trash
)

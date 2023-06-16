package com.deslomator.tagtimer.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.ui.Screen

data class SessionsTrashState(
    val activeScreen: MutableState<String> = mutableStateOf(Screen.SESSIONS_TRASH.name),
    val sessions: List<Session> = emptyList(),
    val currentSession: Session = Session(),
    val showSnackBar: Boolean = false,
    val snackbarMessage: Int = R.string.session_sent_to_trash
)

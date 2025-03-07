package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.type.DialogState
import com.deslomator.tagtimer.model.type.PreferenceProvider

data class SessionsScreenState(
    val sessions: List<Session> = emptyList(),
    val preferenceProvider: PreferenceProvider = PreferenceProvider(),
    val currentSession: Session = Session(),
    val sessionDialogState: DialogState = DialogState.HIDDEN,
)

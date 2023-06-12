package com.deslomator.tagtimer.actions

import com.deslomator.tagtimer.model.Session

sealed interface SessionAction {
    data class UpsertSession(val session: Session): SessionAction
    data class DeleteSession(val session: Session): SessionAction
    data class EditSession(val session: Session): SessionAction
}
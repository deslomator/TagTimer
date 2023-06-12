package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Session

sealed interface SessionAction {
    data class UpsertSession(val session: Session): SessionAction
    data class DeleteSession(val session: Session): SessionAction
    data class EditSession(val session: Session, val editingIndex: Int): SessionAction
    data class UpdateName(val name: String): SessionAction
    data class UpdateColor(val color: Long): SessionAction
    data class SessionEdited(val session: Session): SessionAction
    object AddSession: SessionAction
    data class SessionAdded(val session: Session): SessionAction
    object ShowDeleteDialog: SessionAction
    object HideDeleteDialog: SessionAction
}
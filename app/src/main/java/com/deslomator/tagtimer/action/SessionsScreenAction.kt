package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Session

sealed interface SessionsScreenAction {
    data class EditSessionClicked(val session: Session): SessionsScreenAction
    data class AcceptSessionEditionClicked(val session: Session): SessionsScreenAction
    object AddNewSessionClicked: SessionsScreenAction
    data class AcceptAddNewSessionClicked(val session: Session): SessionsScreenAction
    object DismissSessionDialog: SessionsScreenAction
    data class UpdateSessionName(val name: String): SessionsScreenAction
    data class UpdateSessionColor(val color: Int): SessionsScreenAction
    data class TrashSessionSwiped(val session: Session): SessionsScreenAction
    object HideSnackbar: SessionsScreenAction
    data class SessionItemClicked(val session: Session): SessionsScreenAction
    object ManageTagsClicked: SessionsScreenAction
}
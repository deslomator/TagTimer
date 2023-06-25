package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Session

sealed interface SessionsScreenAction {
    object AddNewSessionClicked: SessionsScreenAction
    object AcceptAddSessionClicked: SessionsScreenAction
    object DismissSessionDialog: SessionsScreenAction
    data class UpdateSessionName(val name: String): SessionsScreenAction
    data class UpdateSessionColor(val color: Int): SessionsScreenAction
    data class TrashSessionSwiped(val session: Session): SessionsScreenAction
    object HideSnackbar: SessionsScreenAction
}
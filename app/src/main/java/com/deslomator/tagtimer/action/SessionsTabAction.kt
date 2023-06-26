package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Session

sealed interface SessionsTabAction {
    object AddNewSessionClicked: SessionsTabAction
    object AcceptAddSessionClicked: SessionsTabAction
    object DismissSessionDialog: SessionsTabAction
    data class UpdateSessionName(val name: String): SessionsTabAction
    data class UpdateSessionColor(val color: Int): SessionsTabAction
    data class TrashSessionSwiped(val session: Session): SessionsTabAction
}
package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Session

sealed interface SessionsTabAction {
    object AddNewSessionClicked: SessionsTabAction
    data class AcceptAddSessionClicked(val session: Session): SessionsTabAction
    object DismissSessionDialog: SessionsTabAction
    data class TrashSessionSwiped(val session: Session): SessionsTabAction
}
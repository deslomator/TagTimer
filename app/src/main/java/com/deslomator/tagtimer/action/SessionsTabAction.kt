package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Session

sealed interface SessionsTabAction {
    object AddNewSessionClicked: SessionsTabAction
    object PopulateDbClicked: SessionsTabAction
    data class ItemClicked(val session: Session): SessionsTabAction
    data class DialogAcceptClicked(val session: Session): SessionsTabAction
    object DismissSessionDialog: SessionsTabAction
    data class TrashSessionSwiped(val session: Session): SessionsTabAction
}
package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.type.SessionSort

sealed interface SessionsTabAction {
    data object AddNewSessionClicked: SessionsTabAction
    data object PopulateDbClicked: SessionsTabAction
    class SessionSortClicked(val sessionSort: SessionSort): SessionsTabAction
    class ItemClicked(val session: Session): SessionsTabAction
    class DialogAcceptClicked(val session: Session): SessionsTabAction
    data object DismissSessionDialog: SessionsTabAction
    data object TrashSessionClicked: SessionsTabAction
    class CopySessionClicked(val copyString: String): SessionsTabAction
}
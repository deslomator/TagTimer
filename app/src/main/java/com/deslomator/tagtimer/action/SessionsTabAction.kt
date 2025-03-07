package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.type.SessionSort

sealed interface SessionsTabAction {
    data object AddNewSessionClicked: SessionsTabAction
    data object PopulateDbClicked: SessionsTabAction
    class SessionSortClicked(val sessionSort: SessionSort): SessionsTabAction
    class ShowEnabledClicked(val show: Boolean): SessionsTabAction
    class ShowArchivedClicked(val show: Boolean): SessionsTabAction
    class ShowTrashedClicked(val show: Boolean): SessionsTabAction
    class ItemClicked(val session: Session): SessionsTabAction
    class DialogAcceptClicked(val session: Session): SessionsTabAction
    data object DismissSessionDialog: SessionsTabAction
    data object ArchiveSessionClicked: SessionsTabAction
    data object TrashSessionClicked: SessionsTabAction
    class CopySessionClicked(val copyString: String): SessionsTabAction
}
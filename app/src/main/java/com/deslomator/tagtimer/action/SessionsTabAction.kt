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

    class EditSessionClicked(val session: Session): SessionsTabAction
    class DialogAcceptClicked(val session: Session): SessionsTabAction
    data object DismissSessionDialog: SessionsTabAction

    data object ArchiveSessionClicked: SessionsTabAction
    data object UnArchiveSessionClicked: SessionsTabAction
    data object TrashSessionClicked: SessionsTabAction
    data object UnTrashSessionClicked: SessionsTabAction
    data object PurgeSessionClicked: SessionsTabAction
    class CopySessionClicked(val copyString: String): SessionsTabAction
}
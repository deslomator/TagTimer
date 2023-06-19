package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.Tag

sealed interface SessionsTrashAction {
    object ShowSessionsClicked: SessionsTrashAction
    object ShowTagsClicked: SessionsTrashAction
    data class DeleteSessionClicked(val session: Session): SessionsTrashAction
    data class RestoreSessionClicked(val session: Session): SessionsTrashAction
    data class DeleteTagClicked(val tag: Tag): SessionsTrashAction
    data class RestoreTagClicked(val tag: Tag): SessionsTrashAction
    object HideSnackbar: SessionsTrashAction
}
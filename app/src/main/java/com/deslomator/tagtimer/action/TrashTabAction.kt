package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.Tag

sealed interface TrashTabAction {
    object ShowSessionsClicked: TrashTabAction
    object ShowTagsClicked: TrashTabAction
    data class DeleteSessionClicked(val session: Session): TrashTabAction
    data class RestoreSessionClicked(val session: Session): TrashTabAction
    data class DeleteTagClicked(val tag: Tag): TrashTabAction
    data class RestoreTagClicked(val tag: Tag): TrashTabAction
    object HideSnackbar: TrashTabAction
}
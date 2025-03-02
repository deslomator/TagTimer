package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Session

sealed interface TrashTabAction {
    class DeleteSessionClicked(val session: Session): TrashTabAction
    class RestoreSessionClicked(val session: Session): TrashTabAction
    class DeleteLabelClicked(val tag: Label): TrashTabAction
    class RestoreLabelClicked(val tag: Label): TrashTabAction
}
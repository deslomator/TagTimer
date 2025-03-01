package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Session

sealed interface TrashTabAction {
    class DeleteSessionClicked(val session: Session): TrashTabAction
    class RestoreSessionClicked(val session: Session): TrashTabAction
    class DeleteTagClicked(val tag: Label): TrashTabAction
    class RestoreTagClicked(val tag: Label): TrashTabAction
    class DeletePersonClicked(val person: Label): TrashTabAction
    class RestorePersonClicked(val person: Label): TrashTabAction
    class DeletePlaceClicked(val place: Label): TrashTabAction
    class RestorePlaceClicked(val place: Label): TrashTabAction
}
package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Lbl
import com.deslomator.tagtimer.model.Session

sealed interface TrashTabAction {
    data class DeleteSessionClicked(val session: Session): TrashTabAction
    data class RestoreSessionClicked(val session: Session): TrashTabAction
    data class DeleteTagClicked(val tag: Lbl.Tag): TrashTabAction
    data class RestoreTagClicked(val tag: Lbl.Tag): TrashTabAction
    data class DeletePersonClicked(val person: Lbl.Person): TrashTabAction
    data class RestorePersonClicked(val person: Lbl.Person): TrashTabAction
    data class DeletePlaceClicked(val place: Lbl.Place): TrashTabAction
    data class RestorePlaceClicked(val place: Lbl.Place): TrashTabAction
}
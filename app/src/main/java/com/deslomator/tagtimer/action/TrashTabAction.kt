package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Session

sealed interface TrashTabAction {
    data class DeleteSessionClicked(val session: Session): TrashTabAction
    data class RestoreSessionClicked(val session: Session): TrashTabAction
    data class DeleteTagClicked(val tag: Label.Tag): TrashTabAction
    data class RestoreTagClicked(val tag: Label.Tag): TrashTabAction
    data class DeletePersonClicked(val person: Label.Person): TrashTabAction
    data class RestorePersonClicked(val person: Label.Person): TrashTabAction
    data class DeletePlaceClicked(val place: Label.Place): TrashTabAction
    data class RestorePlaceClicked(val place: Label.Place): TrashTabAction
}
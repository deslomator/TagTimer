package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Session

sealed interface TrashTabAction {
    class DeleteSessionClicked(val session: Session): TrashTabAction
    class RestoreSessionClicked(val session: Session): TrashTabAction
    class DeleteTagClicked(val tag: Label.Tag): TrashTabAction
    class RestoreTagClicked(val tag: Label.Tag): TrashTabAction
    class DeletePersonClicked(val person: Label.Person): TrashTabAction
    class RestorePersonClicked(val person: Label.Person): TrashTabAction
    class DeletePlaceClicked(val place: Label.Place): TrashTabAction
    class RestorePlaceClicked(val place: Label.Place): TrashTabAction
}
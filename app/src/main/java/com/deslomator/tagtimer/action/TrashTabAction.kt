package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Person
import com.deslomator.tagtimer.model.Place
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.Tag
import com.deslomator.tagtimer.model.type.Trash

sealed interface TrashTabAction {
    data class DeleteSessionClicked(val session: Session): TrashTabAction
    data class RestoreSessionClicked(val session: Session): TrashTabAction
    data class DeleteTagClicked(val tag: Tag): TrashTabAction
    data class RestoreTagClicked(val tag: Tag): TrashTabAction
    data class DeletePersonClicked(val person: Person): TrashTabAction
    data class RestorePersonClicked(val person: Person): TrashTabAction
    data class DeletePlaceClicked(val place: Place): TrashTabAction
    data class RestorePlaceClicked(val place: Place): TrashTabAction
}
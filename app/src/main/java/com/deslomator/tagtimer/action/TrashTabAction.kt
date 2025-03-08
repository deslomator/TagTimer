package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.ancillary.EventForDisplay
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Session

sealed interface TrashTabAction {
    class DeleteSessionClicked(val session: Session): TrashTabAction
    class RestoreSessionClicked(val session: Session): TrashTabAction

    class DeleteLabelClicked(val tag: Label): TrashTabAction
    class RestoreLabelClicked(val tag: Label): TrashTabAction


    class EventInTrashClicked(val event: EventForDisplay): TrashTabAction
    data object DismissEventInTrashDialog: TrashTabAction
    class DeleteEventClicked(val event4d: EventForDisplay): TrashTabAction
    class RestoreEventClicked(val event4d: EventForDisplay): TrashTabAction
}
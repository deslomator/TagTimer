package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.ancillary.EventForDisplay

sealed interface TrashTabAction {
    class EventInTrashClicked(val event: EventForDisplay): TrashTabAction
    data object DismissEventInTrashDialog: TrashTabAction
    class DeleteEventClicked(val event4d: EventForDisplay): TrashTabAction
    class RestoreEventClicked(val event4d: EventForDisplay): TrashTabAction
}
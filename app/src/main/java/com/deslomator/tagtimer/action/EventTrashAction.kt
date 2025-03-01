package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.EventForDisplay

sealed interface EventTrashAction {
    class EventInTrashClicked(val event: EventForDisplay): EventTrashAction
    data object DismissEventInTrashDialog: EventTrashAction
    class RestoreEventClicked(val event4d: EventForDisplay): EventTrashAction
    class DeleteEventClicked(val event4d: EventForDisplay): EventTrashAction


}
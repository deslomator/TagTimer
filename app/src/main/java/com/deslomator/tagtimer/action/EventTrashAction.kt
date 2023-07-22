package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Event

sealed interface EventTrashAction {
    class EventInTrashClicked(val event: Event): EventTrashAction
    object DismissEventInTrashDialog: EventTrashAction
    class RestoreEventClicked(val event: Event): EventTrashAction
    class DeleteEventClicked(val event: Event): EventTrashAction


}
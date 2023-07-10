package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Event

sealed interface EventTrashAction {
    data class EventInTrashClicked(val event: Event): EventTrashAction
    object DismissEventInTrashDialog: EventTrashAction
    data class RestoreEventClicked(val event: Event): EventTrashAction
    data class DeleteEventClicked(val event: Event): EventTrashAction


}
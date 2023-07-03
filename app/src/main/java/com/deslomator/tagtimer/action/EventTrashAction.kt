package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Label

sealed interface EventTrashAction {
    class SetCursor(val time: Long): EventTrashAction
    class IncreaseCursor(val stepMillis: Long): EventTrashAction
    object StopSession: EventTrashAction
    /*
    ACTIVE
     */
    object ExportSessionClicked: EventTrashAction
    object SessionExported: EventTrashAction
    class EventClicked(val event: Event): EventTrashAction
    class AcceptEventEditionClicked(val event: Event) : EventTrashAction
    object DismissEventEditionDialog: EventTrashAction
    class TrashEventSwiped(val event: Event): EventTrashAction
    object TimeClicked: EventTrashAction
    object DismissTimeDialog: EventTrashAction
    object PlayPauseClicked: EventTrashAction
    class UsedTagClicked(val tagName: String) : EventTrashAction
    class PreSelectedTagClicked(val tag: Label.Tag) : EventTrashAction
    class PreSelectedPersonClicked(val personName: String) : EventTrashAction
    class PreSelectedPlaceClicked(val placeName: String) : EventTrashAction
    /*
    SELECT
     */
    class SelectTagCheckedChange(val tagId: Int, val checked: Boolean): EventTrashAction
    class SelectPersonCheckedChange(val personId: Int, val checked: Boolean): EventTrashAction
    class SelectPlaceCheckedChange(val placeId: Int, val checked: Boolean): EventTrashAction
    /*
    FILTER
     */
    data class ExportFilteredEventsClicked(val filteredEvents: List<Event>): EventTrashAction
    /*
    TRASH
     */
    class EventInTrashClicked(val event: Event): EventTrashAction
    object DismissEventInTrashDialog: EventTrashAction
    class RestoreEventClicked(val event: Event): EventTrashAction
    class DeleteEventClicked(val event: Event): EventTrashAction


}
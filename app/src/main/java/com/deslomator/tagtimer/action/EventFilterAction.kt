package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Label

sealed interface EventFilterAction {
    class SetCursor(val time: Long): EventFilterAction
    class IncreaseCursor(val stepMillis: Long): EventFilterAction
    object StopSession: EventFilterAction
    /*
    ACTIVE
     */
    object ExportSessionClicked: EventFilterAction
    object EventsExported: EventFilterAction
    class EventClicked(val event: Event): EventFilterAction
    class AcceptEventEditionClicked(val event: Event) : EventFilterAction
    object DismissEventEditionDialog: EventFilterAction
    class TrashEventSwiped(val event: Event): EventFilterAction
    object TimeClicked: EventFilterAction
    object DismissTimeDialog: EventFilterAction
    object PlayPauseClicked: EventFilterAction
    class UsedTagClicked(val tagName: String) : EventFilterAction
    class PreSelectedTagClicked(val tag: Label.Tag) : EventFilterAction
    class PreSelectedPersonClicked(val personName: String) : EventFilterAction
    class PreSelectedPlaceClicked(val placeName: String) : EventFilterAction
    /*
    SELECT
     */
    class SelectTagCheckedChange(val tagId: Int, val checked: Boolean): EventFilterAction
    class SelectPersonCheckedChange(val personId: Int, val checked: Boolean): EventFilterAction
    class SelectPlaceCheckedChange(val placeId: Int, val checked: Boolean): EventFilterAction
    /*
    FILTER
     */
    data class ExportFilteredEventsClicked(val filteredEvents: List<Event>): EventFilterAction
    /*
    TRASH
     */
    class EventInTrashClicked(val event: Event): EventFilterAction
    object DismissEventInTrashDialog: EventFilterAction
    class RestoreEventClicked(val event: Event): EventFilterAction
    class DeleteEventClicked(val event: Event): EventFilterAction


}
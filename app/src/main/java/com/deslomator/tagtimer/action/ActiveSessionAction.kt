package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Lbl

sealed interface ActiveSessionAction {
    class UpdateSessionId(val id: Int): ActiveSessionAction
    class SetCursor(val time: Long): ActiveSessionAction
    class IncreaseCursor(val stepMillis: Long): ActiveSessionAction
    object StopSession: ActiveSessionAction
    /*
    ACTIVE
     */
    object ExportSessionClicked: ActiveSessionAction
    object SessionExported: ActiveSessionAction
    class EventClicked(val event: Event): ActiveSessionAction
    class AcceptEventEditionClicked(val event: Event) : ActiveSessionAction
    object DismissEventEditionDialog: ActiveSessionAction
    class TrashEventSwiped(val event: Event): ActiveSessionAction
    object TimeClicked: ActiveSessionAction
    object DismissTimeDialog: ActiveSessionAction
    object PlayPauseClicked: ActiveSessionAction
    class UsedTagClicked(val tagName: String) : ActiveSessionAction
    class PreSelectedTagClicked(val tag: Lbl.Tag) : ActiveSessionAction
    class PreSelectedPersonClicked(val personName: String) : ActiveSessionAction
    class PreSelectedPlaceClicked(val placeName: String) : ActiveSessionAction
    /*
    SELECT
     */
    class SelectTagCheckedChange(val tagId: Int, val checked: Boolean): ActiveSessionAction
    class SelectPersonCheckedChange(val personId: Int, val checked: Boolean): ActiveSessionAction
    class SelectPlaceCheckedChange(val placeId: Int, val checked: Boolean): ActiveSessionAction
    /*
    FILTER
     */
    data class ExportFilteredEventsClicked(val filteredEvents: List<Event>): ActiveSessionAction
    /*
    TRASH
     */
    class EventInTrashClicked(val event: Event): ActiveSessionAction
    object DismissEventInTrashDialog: ActiveSessionAction
    class RestoreEventClicked(val event: Event): ActiveSessionAction
    class DeleteEventClicked(val event: Event): ActiveSessionAction


}
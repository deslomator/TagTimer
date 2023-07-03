package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Label

sealed interface LabelPreselectionAction {
    class SetCursor(val time: Long): LabelPreselectionAction
    class IncreaseCursor(val stepMillis: Long): LabelPreselectionAction
    object StopSession: LabelPreselectionAction
    /*
    ACTIVE
     */
    object ExportSessionClicked: LabelPreselectionAction
    object SessionExported: LabelPreselectionAction
    class EventClicked(val event: Event): LabelPreselectionAction
    class AcceptEventEditionClicked(val event: Event) : LabelPreselectionAction
    object DismissEventEditionDialog: LabelPreselectionAction
    class TrashEventSwiped(val event: Event): LabelPreselectionAction
    object TimeClicked: LabelPreselectionAction
    object DismissTimeDialog: LabelPreselectionAction
    object PlayPauseClicked: LabelPreselectionAction
    class UsedTagClicked(val tagName: String) : LabelPreselectionAction
    class PreSelectedTagClicked(val tag: Label.Tag) : LabelPreselectionAction
    class PreSelectedPersonClicked(val personName: String) : LabelPreselectionAction
    class PreSelectedPlaceClicked(val placeName: String) : LabelPreselectionAction
    /*
    SELECT
     */
    class SelectTagCheckedChange(val tagId: Int, val checked: Boolean): LabelPreselectionAction
    class SelectPersonCheckedChange(val personId: Int, val checked: Boolean): LabelPreselectionAction
    class SelectPlaceCheckedChange(val placeId: Int, val checked: Boolean): LabelPreselectionAction
    /*
    FILTER
     */
    data class ExportFilteredEventsClicked(val filteredEvents: List<Event>): LabelPreselectionAction
    /*
    TRASH
     */
    class EventInTrashClicked(val event: Event): LabelPreselectionAction
    object DismissEventInTrashDialog: LabelPreselectionAction
    class RestoreEventClicked(val event: Event): LabelPreselectionAction
    class DeleteEventClicked(val event: Event): LabelPreselectionAction


}
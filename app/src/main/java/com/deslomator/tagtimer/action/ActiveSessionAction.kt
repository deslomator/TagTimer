package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.Tag

sealed interface ActiveSessionAction {
    class UpdateSessionId(val id: Int): ActiveSessionAction
    object PlayPauseClicked: ActiveSessionAction
    object DismissTagDialog: ActiveSessionAction
    object SelectTagsClicked: ActiveSessionAction
    class SelectTagCheckedChange(val tagId: Int, val checked: Boolean): ActiveSessionAction
    class SelectPersonCheckedChange(val personId: Int, val checked: Boolean): ActiveSessionAction
    class SelectPlaceCheckedChange(val placeId: Int, val checked: Boolean): ActiveSessionAction
    object AcceptTagSelectionClicked: ActiveSessionAction
    object StopSession: ActiveSessionAction
    class PreSelectedTagClicked(val tag: Tag) : ActiveSessionAction
    object EventTrashClicked: ActiveSessionAction
    object DismissEventTrashDialog: ActiveSessionAction
    class RestoreEventClicked(val event: Event): ActiveSessionAction
    class DeleteEventClicked(val event: Event): ActiveSessionAction
    class TrashEventSwiped(val eventId: Int): ActiveSessionAction
    object ExportSessionClicked: ActiveSessionAction
    object SessionExported: ActiveSessionAction
    object EditSessionClicked: ActiveSessionAction
    class AcceptSessionEditionClicked(val session: Session): ActiveSessionAction
    object DismissSessionEditionDialog: ActiveSessionAction
    class EventClicked(val event: Event): ActiveSessionAction
    class AcceptEventEditionClicked(val event: Event) : ActiveSessionAction
    object DismissEventEditionDialog: ActiveSessionAction
    class EventInTrashClicked(val event: Event): ActiveSessionAction
    object DismissEventInTrashDialog: ActiveSessionAction
    object TimeClicked: ActiveSessionAction
    class SetCursor(val time: Long): ActiveSessionAction
    class IncreaseCursor(val stepMillis: Long): ActiveSessionAction
    object DismissTimeDialog: ActiveSessionAction
    data class LabelTypeSelected(val type: Label): ActiveSessionAction

}
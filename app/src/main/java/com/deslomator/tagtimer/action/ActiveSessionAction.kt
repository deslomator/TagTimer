package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Tag

sealed interface ActiveSessionAction {
    class UpdateSessionId(val id: Int): ActiveSessionAction
    object PlayPauseClicked: ActiveSessionAction
    object DismissTagDialog: ActiveSessionAction
    object SelectTagsClicked: ActiveSessionAction
    class SelectTagCheckedChange(val tagId: Int, val checked: Boolean): ActiveSessionAction
    object AcceptTagSelectionClicked: ActiveSessionAction
    object StopSession: ActiveSessionAction
    class PreSelectedTagClicked(val tag: Tag) : ActiveSessionAction
    class AcceptEventNoteChanged(val event: Event, val note: String) : ActiveSessionAction
}
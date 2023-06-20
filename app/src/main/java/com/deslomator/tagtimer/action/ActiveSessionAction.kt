package com.deslomator.tagtimer.action

sealed interface ActiveSessionAction {
    class UpdateSessionId(val id: Int): ActiveSessionAction
    object PlayPauseClicked: ActiveSessionAction
    object DismissTagDialog: ActiveSessionAction
    object SelectTagsClicked: ActiveSessionAction
    class SelectTagClicked(tagId: Int): ActiveSessionAction
    object AcceptTagSelectionClicked: ActiveSessionAction
}
package com.deslomator.tagtimer.action

sealed interface ActiveSessionAction {
    class UpdateSessionId(val id: Int): ActiveSessionAction
    object PlayPauseClicked: ActiveSessionAction
    object AddTagClicked: ActiveSessionAction
}
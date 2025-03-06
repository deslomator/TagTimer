package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.EventForDisplay
import com.deslomator.tagtimer.model.Label

sealed interface ActiveSessionAction {
    data object ExitSession: ActiveSessionAction
    data object ShareSessionClicked: ActiveSessionAction
    data object PlayPauseClicked: ActiveSessionAction
    data object SessionShared: ActiveSessionAction
    class EventClicked(val event4d: EventForDisplay): ActiveSessionAction
    class AcceptEventEditionClicked(val event4d: EventForDisplay) : ActiveSessionAction
    data object DismissEventEditionDialog: ActiveSessionAction
    class TrashEventSwiped(val event4d: EventForDisplay): ActiveSessionAction
    data object TimeClicked : ActiveSessionAction
    class AcceptTimeDialog(val newTime: Long): ActiveSessionAction
    data object DismissTimeDialog: ActiveSessionAction
    class SelectedTagClicked(val tag: Label) : ActiveSessionAction
    class SelectedPersonClicked(val person: Label) : ActiveSessionAction
    class SelectedPlaceClicked(val place: Label) : ActiveSessionAction
}
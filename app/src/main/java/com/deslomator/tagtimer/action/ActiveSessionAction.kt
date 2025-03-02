package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.EventForDisplay
import com.deslomator.tagtimer.model.Label

sealed interface ActiveSessionAction {
    data object ExitSession: ActiveSessionAction
    data object ShareSessionClicked: ActiveSessionAction
    data object PlayPauseClicked: ActiveSessionAction
    data object SessionShared: ActiveSessionAction
    class EventClicked(val event: EventForDisplay): ActiveSessionAction
    class AcceptEventEditionClicked(val event4d: EventForDisplay) : ActiveSessionAction
    data object DismissEventEditionDialog: ActiveSessionAction
    class TrashEventSwiped(val event4d: EventForDisplay): ActiveSessionAction
    data object TimeClicked : ActiveSessionAction
    class AcceptTimeDialog(val newTime: Long): ActiveSessionAction
    data object DismissTimeDialog: ActiveSessionAction
    class PreSelectedTagClicked(val tag: Label) : ActiveSessionAction
    class PreSelectedPersonClicked(val person: Label) : ActiveSessionAction
    class PreSelectedPlaceClicked(val place: Label) : ActiveSessionAction
}
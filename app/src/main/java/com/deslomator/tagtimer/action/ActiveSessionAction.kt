package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Label

sealed interface ActiveSessionAction {
    data object ExitSession: ActiveSessionAction
    data object ShareSessionClicked: ActiveSessionAction
    data object PlayPauseClicked: ActiveSessionAction
    data object SessionShared: ActiveSessionAction
    class EventClicked(val event: Event): ActiveSessionAction
    class AcceptEventEditionClicked(val event: Event) : ActiveSessionAction
    data object DismissEventEditionDialog: ActiveSessionAction
    class TrashEventSwiped(val event: Event): ActiveSessionAction
    data object TimeClicked : ActiveSessionAction
    class AcceptTimeDialog(val offset: Long): ActiveSessionAction
    data object DismissTimeDialog: ActiveSessionAction
    class PreSelectedTagClicked(val tag: Label.Tag, val elapsed: Long) : ActiveSessionAction
    class PreSelectedPersonClicked(val personName: String) : ActiveSessionAction
    class PreSelectedPlaceClicked(val placeName: String) : ActiveSessionAction
}
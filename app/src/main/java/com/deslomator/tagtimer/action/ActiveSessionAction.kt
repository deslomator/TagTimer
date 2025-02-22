package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Label

sealed interface ActiveSessionAction {
    class ExitSession(val isRunning: Boolean, val cursor: Long): ActiveSessionAction
    data object ShareSessionClicked: ActiveSessionAction
    data object PlayPauseClicked: ActiveSessionAction
    data object SessionExported: ActiveSessionAction
    class EventClicked(val event: Event): ActiveSessionAction
    class AcceptEventEditionClicked(val event: Event) : ActiveSessionAction
    data object DismissEventEditionDialog: ActiveSessionAction
    class TrashEventSwiped(val event: Event): ActiveSessionAction
    class TimeClicked(val cursor: Long): ActiveSessionAction
    data object DismissTimeDialog: ActiveSessionAction
    class PreSelectedTagClicked(val tag: Label.Tag, val cursor: Long) : ActiveSessionAction
    class PreSelectedPersonClicked(val personName: String) : ActiveSessionAction
    class PreSelectedPlaceClicked(val placeName: String) : ActiveSessionAction

}
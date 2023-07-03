package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Label

sealed interface ActiveSessionAction {
    class SetCursor(val time: Long): ActiveSessionAction
    class IncreaseCursor(val stepMillis: Long): ActiveSessionAction
    object StopSession: ActiveSessionAction
    object ExportSessionClicked: ActiveSessionAction
    object SessionExported: ActiveSessionAction
    class EventClicked(val event: Event): ActiveSessionAction
    class AcceptEventEditionClicked(val event: Event) : ActiveSessionAction
    object DismissEventEditionDialog: ActiveSessionAction
    class TrashEventSwiped(val event: Event): ActiveSessionAction
    object TimeClicked: ActiveSessionAction
    object DismissTimeDialog: ActiveSessionAction
    object PlayPauseClicked: ActiveSessionAction
    class PreSelectedTagClicked(val tag: Label.Tag) : ActiveSessionAction
    class PreSelectedPersonClicked(val personName: String) : ActiveSessionAction
    class PreSelectedPlaceClicked(val placeName: String) : ActiveSessionAction

}
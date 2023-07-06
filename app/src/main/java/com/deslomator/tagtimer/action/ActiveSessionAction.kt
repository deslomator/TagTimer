package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Label

sealed interface ActiveSessionAction {
    object UpdateSession: ActiveSessionAction
    object ExportSessionClicked: ActiveSessionAction
    object SessionExported: ActiveSessionAction
    class EventClicked(val event: Event): ActiveSessionAction
    class AcceptEventEditionClicked(val event: Event) : ActiveSessionAction
    object DismissEventEditionDialog: ActiveSessionAction
    class TrashEventSwiped(val event: Event): ActiveSessionAction
    object TimeClicked: ActiveSessionAction
    object DismissTimeDialog: ActiveSessionAction
    class PreSelectedTagClicked(val tag: Label.Tag, val cursor: Long) : ActiveSessionAction
    class PreSelectedPersonClicked(val personName: String) : ActiveSessionAction
    class PreSelectedPlaceClicked(val placeName: String) : ActiveSessionAction

}
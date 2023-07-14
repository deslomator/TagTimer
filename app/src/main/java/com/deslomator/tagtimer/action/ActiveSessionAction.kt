package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Label

sealed interface ActiveSessionAction {
    data class ExitSession(val isRunning: Boolean, val cursor: Long): ActiveSessionAction
    object ExportSessionClicked: ActiveSessionAction
    object PlayPauseClicked: ActiveSessionAction
    object SessionExported: ActiveSessionAction
    data class EventClicked(val event: Event): ActiveSessionAction
    data class AcceptEventEditionClicked(val event: Event) : ActiveSessionAction
    object DismissEventEditionDialog: ActiveSessionAction
    data class TrashEventSwiped(val event: Event): ActiveSessionAction
    data class TimeClicked(val cursor: Long): ActiveSessionAction
    object DismissTimeDialog: ActiveSessionAction
    data class PreSelectedTagClicked(val tag: Label.Tag, val cursor: Long) : ActiveSessionAction
    data class PreSelectedPersonClicked(val personName: String) : ActiveSessionAction
    data class PreSelectedPlaceClicked(val placeName: String) : ActiveSessionAction

}
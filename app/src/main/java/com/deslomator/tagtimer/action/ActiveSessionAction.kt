package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Event

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
    class AcceptTimeDialog(val newTime: Long): ActiveSessionAction
    data object DismissTimeDialog: ActiveSessionAction
    class PreSelectedTagClicked(val tagId: Long?, val color: String, val elapsed: Long) : ActiveSessionAction
    class PreSelectedPersonClicked(val personId: Long?) : ActiveSessionAction
    class PreSelectedPlaceClicked(val placeId: Long?) : ActiveSessionAction
}
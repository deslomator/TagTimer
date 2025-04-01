package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.ancillary.EventForDisplay

sealed interface EventFilterAction {
    data object EventsExported: EventFilterAction
    class EventClicked(val event4d: EventForDisplay): EventFilterAction
    class AcceptEventEditionClicked(val event4d: EventForDisplay) : EventFilterAction // this is an Event, not an E4D
    data object DismissEventEditionDialog: EventFilterAction
    class UsedTagClicked(val tag: Label) : EventFilterAction
    class UsedPersonClicked(val person: Label) : EventFilterAction
    class UsedPlaceClicked(val place: Label) : EventFilterAction
    class ExportFilteredEventsClicked(val filteredEvents: List<EventForDisplay>): EventFilterAction


}
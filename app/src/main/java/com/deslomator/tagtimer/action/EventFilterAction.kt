package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Event

sealed interface EventFilterAction {
    object EventsExported: EventFilterAction
    class EventClicked(val event: Event): EventFilterAction
    class AcceptEventEditionClicked(val event: Event) : EventFilterAction
    object DismissEventEditionDialog: EventFilterAction
    class UsedTagClicked(val tagName: String) : EventFilterAction
    class PreSelectedPersonClicked(val personName: String) : EventFilterAction
    class PreSelectedPlaceClicked(val placeName: String) : EventFilterAction
    data class ExportFilteredEventsClicked(val filteredEvents: List<Event>): EventFilterAction


}
package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.type.Sort

sealed interface EventFilterAction {
    object EventsExported: EventFilterAction
    class EventClicked(val event: Event): EventFilterAction
    class AcceptEventEditionClicked(val event: Event) : EventFilterAction
    object DismissEventEditionDialog: EventFilterAction
    class UsedTagClicked(val tagName: String) : EventFilterAction
    class usedPersonClicked(val personName: String) : EventFilterAction
    class usedPlaceClicked(val placeName: String) : EventFilterAction
    data class ExportFilteredEventsClicked(val filteredEvents: List<Event>): EventFilterAction
    data class SetTagSort(val tagSort: Sort): EventFilterAction
    data class SetPersonSort(val personSort: Sort): EventFilterAction
    data class SetPlaceSort(val placeSort: Sort): EventFilterAction


}
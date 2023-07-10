package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.type.Sort

sealed interface EventFilterAction {
    object EventsExported: EventFilterAction
    data class EventClicked(val event: Event): EventFilterAction
    data class AcceptEventEditionClicked(val event: Event) : EventFilterAction
    object DismissEventEditionDialog: EventFilterAction
    data class UsedTagClicked(val tag: Label.Tag) : EventFilterAction
    data class UsedPersonClicked(val personName: String) : EventFilterAction
    data class UsedPlaceClicked(val placeName: String) : EventFilterAction
    data class ExportFilteredEventsClicked(val filteredEvents: List<Event>): EventFilterAction
    data class SetTagSort(val tagSort: Sort): EventFilterAction
    data class SetPersonSort(val personSort: Sort): EventFilterAction
    data class SetPlaceSort(val placeSort: Sort): EventFilterAction


}
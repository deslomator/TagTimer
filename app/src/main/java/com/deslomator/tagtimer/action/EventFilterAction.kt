package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.type.Sort

sealed interface EventFilterAction {
    data object EventsExported: EventFilterAction
    class EventClicked(val event: Event): EventFilterAction
    class AcceptEventEditionClicked(val event: Event) : EventFilterAction
    data object DismissEventEditionDialog: EventFilterAction
    class UsedTagClicked(val tag: Label.Tag) : EventFilterAction
    class UsedPersonClicked(val personName: String) : EventFilterAction
    class UsedPlaceClicked(val placeName: String) : EventFilterAction
    class ExportFilteredEventsClicked(val filteredEvents: List<Event>): EventFilterAction
    class SetTagSort(val tagSort: Sort): EventFilterAction
    class SetPersonSort(val personSort: Sort): EventFilterAction
    class SetPlaceSort(val placeSort: Sort): EventFilterAction


}
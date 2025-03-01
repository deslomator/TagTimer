package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.EventForDisplay
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.type.LabelSort

sealed interface EventFilterAction {
    data object EventsExported: EventFilterAction
    class EventClicked(val event: EventForDisplay): EventFilterAction
    class AcceptEventEditionClicked(val event: Event) : EventFilterAction
    data object DismissEventEditionDialog: EventFilterAction
    class UsedTagClicked(val tag: Label) : EventFilterAction
    class UsedPersonClicked(val personName: String) : EventFilterAction
    class UsedPlaceClicked(val placeName: String) : EventFilterAction
    class ExportFilteredEventsClicked(val filteredEvents: List<Event>): EventFilterAction
    class SetTagSort(val labelSort: LabelSort): EventFilterAction
    class SetPersonSort(val personSort: LabelSort): EventFilterAction
    class SetPlaceSort(val placeSort: LabelSort): EventFilterAction


}
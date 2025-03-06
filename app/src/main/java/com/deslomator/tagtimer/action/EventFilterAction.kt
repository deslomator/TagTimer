package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.EventForDisplay
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.type.LabelSort

sealed interface EventFilterAction {
    data object EventsExported: EventFilterAction
    class EventClicked(val event4d: EventForDisplay): EventFilterAction
    class AcceptEventEditionClicked(val event: Event) : EventFilterAction // this is an Event, not an E4D
    data object DismissEventEditionDialog: EventFilterAction
    class UsedTagClicked(val tag: Label) : EventFilterAction
    class UsedPersonClicked(val person: Label) : EventFilterAction
    class UsedPlaceClicked(val place: Label) : EventFilterAction
    class ExportFilteredEventsClicked(val filteredEvents: List<EventForDisplay>): EventFilterAction
    class SetTagSort(val labelSort: LabelSort): EventFilterAction
    class SetPersonSort(val personSort: LabelSort): EventFilterAction
    class SetPlaceSort(val placeSort: LabelSort): EventFilterAction


}
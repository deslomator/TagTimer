package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Tag

sealed interface EventAction {
    data class AppendEvent(val tag: Tag, val timestamp: Long, val sessionId: Int): EventAction
    data class DeleteEvent(val event: Event): EventAction
    data class NoteEdited(val event: Event, val note: String): EventAction

//    data class GetEvents(val events: Flow<List<Event>>): EventAction
    object EditNote: EventAction
}
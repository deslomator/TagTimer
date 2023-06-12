package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Tag

sealed interface EventAction {
    data class AppendEvent(val tag: Tag, val timestamp: Long, val sessionId: Int): EventAction
    data class DeleteEvent(val event: Event): EventAction
    data class UpdateNote(val noteText: String): EventAction
    data class EditNote(val event: Event, val editingIndex: Int): EventAction
    data class NoteEdited(val event: Event): EventAction
}
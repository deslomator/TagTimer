package com.deslomator.tagtimer.actions

import com.deslomator.tagtimer.model.Event

sealed interface EventAction {
    data class UpsertEvent(val event: Event): EventAction
    data class DeleteEvent(val event: Event): EventAction
    data class EditNotes(val event: Event, val notes: String): EventAction
}
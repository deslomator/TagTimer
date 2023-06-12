package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.Tag
import com.deslomator.tagtimer.model.UsedTag

sealed interface Action {
    data class AppendEvent(val tagId: Int, val timestamp: Long, val sessionId: Int): Action
    data class DeleteEvent(val event: Event): Action
    data class UpdateEventNote(val noteText: String): Action
    data class EditEventNote(val event: Event, val editingIndex: Int): Action
    data class EventNoteEdited(val event: Event): Action
    object ShowDeleteEventDialog: Action
    object HideDeleteEventDialog: Action
    data class UpsertSession(val session: Session): Action
    data class DeleteSession(val session: Session): Action
    data class EditSession(val session: Session, val editingIndex: Int): Action
    data class UpdateSessionName(val name: String): Action
    data class UpdateSessionColor(val color: Long): Action
    data class SessionEdited(val session: Session): Action
    object AddSession: Action
    data class SessionAdded(val session: Session): Action
    object ShowDeleteSessionDialog: Action
    object HideDeleteSessionDialog: Action
    data class UpsertTag(val tag: Tag): Action
    data class DeleteTag(val tag: Tag): Action
    data class EditTag(val tag: Tag, val editingIndex: Int): Action
    data class TagEdited(val tag: Tag): Action
    object ShowDeleteTagDialog: Action
    object HideDeleteTagDialog: Action
    object AddTag: Action
    data class TagAdded(val tag: Tag): Action
    data class UpdateTagLabel(val label: String): Action
    data class UpdateTagColor(val color: Long): Action
    data class UpdateTagCategory(val category: String): Action
    data class UpsertUsedTag(val usedTag: UsedTag): Action
    data class DeleteUsedTag(val usedTag: UsedTag): Action
    object ShowUsedTagDeleteDialog: Action
    object HideUsedTagDeleteDialog: Action
}
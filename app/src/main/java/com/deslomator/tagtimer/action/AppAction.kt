package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.Tag
import com.deslomator.tagtimer.model.UsedTag

sealed interface AppAction {
    data class AppendEvent(val tagId: Int, val timestamp: Long, val sessionId: Int): AppAction
    data class DeleteEvent(val event: Event): AppAction
    data class UpdateEventNote(val noteText: String): AppAction
    data class EditEventNote(val event: Event, val editingIndex: Int): AppAction
    data class EventNoteEdited(val event: Event): AppAction
    object ShowDeleteEventDialog: AppAction
    object HideDeleteEventDialog: AppAction
    data class UpsertSession(val session: Session): AppAction
    data class DeleteSession(val session: Session): AppAction
    data class EditSession(val session: Session, val editingIndex: Int): AppAction
    data class UpdateSessionName(val name: String): AppAction
    data class UpdateSessionColor(val color: Long): AppAction
    data class SessionEdited(val session: Session): AppAction
    object AddSession: AppAction
    data class SessionAdded(val session: Session): AppAction
    object ShowDeleteSessionDialog: AppAction
    object HideDeleteSessionDialog: AppAction
    data class UpsertTag(val tag: Tag): AppAction
    data class DeleteTag(val tag: Tag): AppAction
    data class EditTag(val tag: Tag, val editingIndex: Int): AppAction
    data class TagEdited(val tag: Tag): AppAction
    object ShowDeleteTagDialog: AppAction
    object HideDeleteTagDialog: AppAction
    object AddTag: AppAction
    data class TagAdded(val tag: Tag): AppAction
    data class UpdateTagLabel(val label: String): AppAction
    data class UpdateTagColor(val color: Long): AppAction
    data class UpdateTagCategory(val category: String): AppAction
    data class UpsertUsedTag(val usedTag: UsedTag): AppAction
    data class DeleteUsedTag(val usedTag: UsedTag): AppAction
    object ShowUsedTagDeleteDialog: AppAction
    object HideUsedTagDeleteDialog: AppAction
}
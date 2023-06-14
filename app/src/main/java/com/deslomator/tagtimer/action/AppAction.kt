package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.Tag
import com.deslomator.tagtimer.model.UsedTag

sealed interface AppAction {
    // Event
    data class AppendEvent(val tagId: Int, val timestamp: Long, val sessionId: Int): AppAction
    data class DeleteEvent(val event: Event): AppAction
    data class UpdateEventNote(val noteText: String): AppAction
    data class EditEventNote(val event: Event): AppAction
    data class EventNoteEdited(val event: Event): AppAction
    object ShowDeleteEventDialog: AppAction
    object HideDeleteEventDialog: AppAction
    // Session
    data class EditSessionClicked(val session: Session): AppAction
    data class AcceptSessionEditionClicked(val session: Session): AppAction
    object AddNewSessionClicked: AppAction
    data class AcceptAddingNewSessionClicked(val session: Session): AppAction
    object DismissSessionDialog: AppAction
    data class UpdateSessionName(val name: String): AppAction
    data class UpdateSessionColor(val color: Int): AppAction
    data class DeleteSessionSwiped(val session: Session): AppAction
//    object AcceptDeleteSessionClicked: AppAction
//    object DismissDeleteSessionDialog: AppAction
    data class SessionItemClicked(val session: Session): AppAction
    // Tag
    data class UpsertTag(val tag: Tag): AppAction
    data class DeleteTag(val tag: Tag): AppAction
    data class EditTag(val tag: Tag): AppAction
    data class TagEdited(val tag: Tag): AppAction
    object ShowDeleteTagDialog: AppAction
    object HideDeleteTagDialog: AppAction
    object AddTag: AppAction
    data class TagAdded(val tag: Tag): AppAction
    data class UpdateTagLabel(val label: String): AppAction
    data class UpdateTagColor(val color: Int): AppAction
    data class UpdateTagCategory(val category: String): AppAction
    // UsedTag
    data class UpsertUsedTag(val usedTag: UsedTag): AppAction
    data class DeleteUsedTag(val usedTag: UsedTag): AppAction
    object ShowUsedTagDeleteDialog: AppAction
    object HideUsedTagDeleteDialog: AppAction
}
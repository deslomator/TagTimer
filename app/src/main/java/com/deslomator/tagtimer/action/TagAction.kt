package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Tag

sealed interface TagAction {
    data class UpsertTag(val tag: Tag): TagAction
    data class DeleteTag(val tag: Tag): TagAction
    data class EditTag(val tag: Tag, val editingIndex: Int): TagAction
    data class TagEdited(val tag: Tag): TagAction
    object ShowDeleteDialog: TagAction
    object HideDeleteDialog: TagAction
    object AddTag: TagAction
    data class TagAdded(val tag: Tag): TagAction
    data class UpdateLabel(val label: String): TagAction
    data class UpdateColor(val color: Long): TagAction
    data class UpdateCategory(val category: String): TagAction
}
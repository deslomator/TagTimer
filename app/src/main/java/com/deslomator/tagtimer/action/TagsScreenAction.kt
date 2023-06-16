package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Tag

sealed interface TagsScreenAction {
    data class UpsertTag(val tag: Tag): TagsScreenAction
    data class DeleteTag(val tag: Tag): TagsScreenAction
    data class EditTag(val tag: Tag): TagsScreenAction
    data class TagEdited(val tag: Tag): TagsScreenAction
    object ShowDeleteTagDialog: TagsScreenAction
    object HideDeleteTagDialog: TagsScreenAction
    object AddTag: TagsScreenAction
    data class TagAdded(val tag: Tag): TagsScreenAction
    data class UpdateTagLabel(val label: String): TagsScreenAction
    data class UpdateTagColor(val color: Int): TagsScreenAction
    data class UpdateTagCategory(val category: String): TagsScreenAction
}
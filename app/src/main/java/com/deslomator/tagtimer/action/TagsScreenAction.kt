package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Tag

sealed interface TagsScreenAction {
    data class EditTagClicked(val tag: Tag): TagsScreenAction
    data class AcceptTagEditionClicked(val tag: Tag): TagsScreenAction
    object AddNewTagClicked: TagsScreenAction
    data class AcceptAddNewTagClicked(val tag: Tag): TagsScreenAction
    object DismissTagDialog: TagsScreenAction
    data class UpdateTagCategory(val category: String): TagsScreenAction
    data class UpdateTagLabel(val label: String): TagsScreenAction
    data class UpdateTagColor(val color: Int): TagsScreenAction
    data class TrashTagSwiped(val tag: Tag): TagsScreenAction
    object HideSnackbar: TagsScreenAction
}
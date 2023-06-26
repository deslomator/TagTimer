package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Tag

sealed interface TagsTabAction {
    data class EditTagClicked(val tag: Tag): TagsTabAction
    data class AcceptTagEditionClicked(val tag: Tag): TagsTabAction
    object AddNewTagClicked: TagsTabAction
    data class AcceptAddNewTagClicked(val tag: Tag): TagsTabAction
    object DismissTagDialog: TagsTabAction
    data class UpdateTagCategory(val category: String): TagsTabAction
    data class UpdateTagLabel(val label: String): TagsTabAction
    data class UpdateTagColor(val color: Int): TagsTabAction
    data class TrashTagSwiped(val tag: Tag): TagsTabAction
}
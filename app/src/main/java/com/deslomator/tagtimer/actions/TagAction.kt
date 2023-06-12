package com.deslomator.tagtimer.actions

import com.deslomator.tagtimer.model.Tag

sealed interface TagAction {
    data class UpsertTag(val tag: Tag): TagAction
    data class DeleteTag(val tag: Tag): TagAction
    data class EditTag(val tag: Tag): TagAction
}
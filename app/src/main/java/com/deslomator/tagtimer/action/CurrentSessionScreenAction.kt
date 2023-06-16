package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.UsedTag

sealed interface CurrentSessionScreenAction {
    data class UpsertUsedTag(val usedTag: UsedTag): CurrentSessionScreenAction
    data class DeleteUsedTag(val usedTag: UsedTag): CurrentSessionScreenAction
    object ShowDeleteUsedTagDialog: CurrentSessionScreenAction
    object HideDeleteUsedTagDialog: CurrentSessionScreenAction
}
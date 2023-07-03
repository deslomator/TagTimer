package com.deslomator.tagtimer.action

sealed interface LabelPreselectionAction {
    class SelectTagCheckedChange(val tagId: Int, val checked: Boolean): LabelPreselectionAction
    class SelectPersonCheckedChange(val personId: Int, val checked: Boolean): LabelPreselectionAction
    class SelectPlaceCheckedChange(val placeId: Int, val checked: Boolean): LabelPreselectionAction
}
package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Label

sealed interface LabelPreselectionAction {
    class SelectTagCheckedChange(val tagId: Int, val checked: Boolean): LabelPreselectionAction
    data class AcceptTagEditionClicked(val tag: Label.Tag): LabelPreselectionAction
    object AddNewTagClicked: LabelPreselectionAction
    object DismissTagDialog: LabelPreselectionAction
    data class TrashTagSwiped(val tag: Label.Tag): LabelPreselectionAction
    class SelectPersonCheckedChange(val personId: Int, val checked: Boolean): LabelPreselectionAction
    data class AcceptPersonEditionClicked(val person: Label.Person): LabelPreselectionAction
    object AddNewPersonClicked: LabelPreselectionAction
    object DismissPersonDialog: LabelPreselectionAction
    data class TrashPersonSwiped(val person: Label.Person): LabelPreselectionAction
    class SelectPlaceCheckedChange(val placeId: Int, val checked: Boolean): LabelPreselectionAction
    data class AcceptPlaceEditionClicked(val place: Label.Place): LabelPreselectionAction
    object AddNewPlaceClicked: LabelPreselectionAction
    object DismissPlaceDialog: LabelPreselectionAction
    data class TrashPlaceSwiped(val place: Label.Place): LabelPreselectionAction
}
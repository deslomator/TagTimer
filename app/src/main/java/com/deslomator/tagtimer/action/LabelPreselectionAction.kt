package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Label

sealed interface LabelPreselectionAction {
    /*
    TAG
     */
    class SelectTagCheckedChange(val tagId: Int, val checked: Boolean): LabelPreselectionAction
    data class EditTagClicked(val tag: Label.Tag): LabelPreselectionAction
    data class AcceptTagEditionClicked(val tag: Label.Tag): LabelPreselectionAction
    object AddNewTagClicked: LabelPreselectionAction
    object DismissTagDialog: LabelPreselectionAction
    data class TrashTagSwiped(val tag: Label.Tag): LabelPreselectionAction
    /*
    PERSON
     */
    class SelectPersonCheckedChange(val personId: Int, val checked: Boolean): LabelPreselectionAction
    data class EditPersonClicked(val person: Label.Person): LabelPreselectionAction
    data class AcceptPersonEditionClicked(val person: Label.Person): LabelPreselectionAction
    object AddNewPersonClicked: LabelPreselectionAction
    object DismissPersonDialog: LabelPreselectionAction
    data class TrashPersonSwiped(val person: Label.Person): LabelPreselectionAction
    /*
    PLACE
     */
    class SelectPlaceCheckedChange(val placeId: Int, val checked: Boolean): LabelPreselectionAction
    data class EditPlaceClicked(val place: Label.Place): LabelPreselectionAction
    data class AcceptPlaceEditionClicked(val place: Label.Place): LabelPreselectionAction
    object AddNewPlaceClicked: LabelPreselectionAction
    object DismissPlaceDialog: LabelPreselectionAction
    data class TrashPlaceSwiped(val place: Label.Place): LabelPreselectionAction
}
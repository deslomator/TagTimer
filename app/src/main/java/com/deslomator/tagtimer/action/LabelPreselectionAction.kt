package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Label

sealed interface LabelPreselectionAction {
    /*
    TAG
     */
    data class SelectTagCheckedChange(val tag: Label.Tag, val checked: Boolean): LabelPreselectionAction
    data class EditTagClicked(val tag: Label.Tag): LabelPreselectionAction
    data class AcceptTagEditionClicked(val tag: Label.Tag): LabelPreselectionAction
    object AddNewTagClicked: LabelPreselectionAction
    object DismissTagDialog: LabelPreselectionAction
    data class DeleteTagClicked(val tag: Label.Tag): LabelPreselectionAction
    /*
    PERSON
     */
    data class SelectPersonCheckedChange(val person: Label.Person, val checked: Boolean): LabelPreselectionAction
    data class EditPersonClicked(val person: Label.Person): LabelPreselectionAction
    data class AcceptPersonEditionClicked(val person: Label.Person): LabelPreselectionAction
    object AddNewPersonClicked: LabelPreselectionAction
    object DismissPersonDialog: LabelPreselectionAction
    data class DeletePersonClicked(val person: Label.Person): LabelPreselectionAction
    /*
    PLACE
     */
    data class SelectPlaceCheckedChange(val place: Label.Place, val checked: Boolean): LabelPreselectionAction
    data class EditPlaceClicked(val place: Label.Place): LabelPreselectionAction
    data class AcceptPlaceEditionClicked(val place: Label.Place): LabelPreselectionAction
    object AddNewPlaceClicked: LabelPreselectionAction
    object DismissPlaceDialog: LabelPreselectionAction
    data class DeletePlaceClicked(val place: Label.Place): LabelPreselectionAction
}
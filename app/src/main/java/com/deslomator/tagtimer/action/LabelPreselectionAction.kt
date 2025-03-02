package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.type.LabelSort

sealed interface LabelPreselectionAction {
    /*
    TAG
     */
    class SelectTagCheckedChange(val tag: Label, val checked: Boolean): LabelPreselectionAction
    class EditTagClicked(val tag: Label): LabelPreselectionAction
    class AcceptTagEditionClicked(val label: Label): LabelPreselectionAction
    data object AddNewTagClicked: LabelPreselectionAction
    data object DismissTagDialog: LabelPreselectionAction
    class DeleteTagClicked(val tag: Label): LabelPreselectionAction
    class SortTagsClicked(val tagSort: LabelSort): LabelPreselectionAction
    /*
    PERSON
     */
    class SelectPersonCheckedChange(val person: Label, val checked: Boolean): LabelPreselectionAction
    class EditPersonClicked(val person: Label): LabelPreselectionAction
    class AcceptPersonEditionClicked(val label: Label): LabelPreselectionAction
    data object AddNewPersonClicked: LabelPreselectionAction
    data object DismissPersonDialog: LabelPreselectionAction
    class DeletePersonClicked(val person: Label): LabelPreselectionAction
    class SortPersonsClicked(val personSort: LabelSort): LabelPreselectionAction
    /*
    PLACE
     */
    class SelectPlaceCheckedChange(val place: Label, val checked: Boolean): LabelPreselectionAction
    class EditPlaceClicked(val place: Label): LabelPreselectionAction
    class AcceptPlaceEditionClicked(val label: Label): LabelPreselectionAction
    data object AddNewPlaceClicked: LabelPreselectionAction
    data object DismissPlaceDialog: LabelPreselectionAction
    class DeletePlaceClicked(val place: Label): LabelPreselectionAction
    class SortPlacesClicked(val placeSort: LabelSort): LabelPreselectionAction
}
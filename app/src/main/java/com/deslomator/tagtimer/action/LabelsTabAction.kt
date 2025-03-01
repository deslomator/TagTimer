package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.type.LabelSort

sealed interface LabelsTabAction {
    /*
    TAG
     */
    class EditTagClicked(val tag: Label): LabelsTabAction
    class AcceptTagEditionClicked(val tag: Label): LabelsTabAction
    data object AddNewTagClicked: LabelsTabAction
    data object DismissTagDialog: LabelsTabAction
    class DeleteTagClicked(val tag: Label): LabelsTabAction
    class TagSortClicked(val labelSort: LabelSort): LabelsTabAction
    /*
    PERSON
     */
    class EditPersonClicked(val person: Label): LabelsTabAction
    class AcceptPersonEditionClicked(val person: Label): LabelsTabAction
    data object AddNewPersonClicked: LabelsTabAction
    data object DismissPersonDialog: LabelsTabAction
    class DeletePersonClicked(val person: Label): LabelsTabAction
    class PersonSortClicked(val personSort: LabelSort): LabelsTabAction
    /*
    PLACE
     */
    class EditPlaceClicked(val place: Label): LabelsTabAction
    class AcceptPlaceEditionClicked(val place: Label): LabelsTabAction
    data object AddNewPlaceClicked: LabelsTabAction
    data object DismissPlaceDialog: LabelsTabAction
    class DeletePlaceClicked(val place: Label): LabelsTabAction
    class PlaceSortClicked(val placeSort: LabelSort): LabelsTabAction
}
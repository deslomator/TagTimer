package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.type.Sort

sealed interface LabelsTabAction {
    /*
    TAG
     */
    data class EditTagClicked(val tag: Label.Tag): LabelsTabAction
    data class AcceptTagEditionClicked(val tag: Label.Tag): LabelsTabAction
    object AddNewTagClicked: LabelsTabAction
    object DismissTagDialog: LabelsTabAction
    data class DeleteTagClicked(val tag: Label.Tag): LabelsTabAction
    data class TagSortClicked(val tagSort: Sort): LabelsTabAction
    /*
    PERSON
     */
    data class EditPersonClicked(val person: Label.Person): LabelsTabAction
    data class AcceptPersonEditionClicked(val person: Label.Person): LabelsTabAction
    object AddNewPersonClicked: LabelsTabAction
    object DismissPersonDialog: LabelsTabAction
    data class DeletePersonClicked(val person: Label.Person): LabelsTabAction
    data class PersonSortClicked(val personSort: Sort): LabelsTabAction
    /*
    PLACE
     */
    data class EditPlaceClicked(val place: Label.Place): LabelsTabAction
    data class AcceptPlaceEditionClicked(val place: Label.Place): LabelsTabAction
    object AddNewPlaceClicked: LabelsTabAction
    object DismissPlaceDialog: LabelsTabAction
    data class DeletePlaceClicked(val place: Label.Place): LabelsTabAction
    data class PlaceSortClicked(val placeSort: Sort): LabelsTabAction
}
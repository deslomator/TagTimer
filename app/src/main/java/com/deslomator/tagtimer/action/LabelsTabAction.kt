package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.type.Sort

sealed interface LabelsTabAction {
    /*
    TAG
     */
    class EditTagClicked(val tag: Label.Tag): LabelsTabAction
    class AcceptTagEditionClicked(val tag: Label.Tag): LabelsTabAction
    object AddNewTagClicked: LabelsTabAction
    object DismissTagDialog: LabelsTabAction
    class DeleteTagClicked(val tag: Label.Tag): LabelsTabAction
    class TagSortClicked(val tagSort: Sort): LabelsTabAction
    /*
    PERSON
     */
    class EditPersonClicked(val person: Label.Person): LabelsTabAction
    class AcceptPersonEditionClicked(val person: Label.Person): LabelsTabAction
    object AddNewPersonClicked: LabelsTabAction
    object DismissPersonDialog: LabelsTabAction
    class DeletePersonClicked(val person: Label.Person): LabelsTabAction
    class PersonSortClicked(val personSort: Sort): LabelsTabAction
    /*
    PLACE
     */
    class EditPlaceClicked(val place: Label.Place): LabelsTabAction
    class AcceptPlaceEditionClicked(val place: Label.Place): LabelsTabAction
    object AddNewPlaceClicked: LabelsTabAction
    object DismissPlaceDialog: LabelsTabAction
    class DeletePlaceClicked(val place: Label.Place): LabelsTabAction
    class PlaceSortClicked(val placeSort: Sort): LabelsTabAction
}
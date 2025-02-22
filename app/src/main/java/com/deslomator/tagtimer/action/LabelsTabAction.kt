package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.type.Sort

sealed interface LabelsTabAction {
    /*
    TAG
     */
    class EditTagClicked(val tag: Label.Tag): LabelsTabAction
    class AcceptTagEditionClicked(val tag: Label.Tag): LabelsTabAction
    data object AddNewTagClicked: LabelsTabAction
    data object DismissTagDialog: LabelsTabAction
    class DeleteTagClicked(val tag: Label.Tag): LabelsTabAction
    class TagSortClicked(val tagSort: Sort): LabelsTabAction
    /*
    PERSON
     */
    class EditPersonClicked(val person: Label.Person): LabelsTabAction
    class AcceptPersonEditionClicked(val person: Label.Person): LabelsTabAction
    data object AddNewPersonClicked: LabelsTabAction
    data object DismissPersonDialog: LabelsTabAction
    class DeletePersonClicked(val person: Label.Person): LabelsTabAction
    class PersonSortClicked(val personSort: Sort): LabelsTabAction
    /*
    PLACE
     */
    class EditPlaceClicked(val place: Label.Place): LabelsTabAction
    class AcceptPlaceEditionClicked(val place: Label.Place): LabelsTabAction
    data object AddNewPlaceClicked: LabelsTabAction
    data object DismissPlaceDialog: LabelsTabAction
    class DeletePlaceClicked(val place: Label.Place): LabelsTabAction
    class PlaceSortClicked(val placeSort: Sort): LabelsTabAction
}
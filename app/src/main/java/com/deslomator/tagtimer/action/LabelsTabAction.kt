package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Person
import com.deslomator.tagtimer.model.Place
import com.deslomator.tagtimer.model.Tag

sealed interface LabelsTabAction {
    data class LabelTypeSelected(val type: Label): LabelsTabAction
    /*
    TAG
     */
    data class EditTagClicked(val tag: Tag): LabelsTabAction
    data class AcceptTagEditionClicked(val tag: Tag): LabelsTabAction
    object AddNewTagClicked: LabelsTabAction
    data class AcceptAddNewTagClicked(val tag: Tag): LabelsTabAction
    object DismissTagDialog: LabelsTabAction
    data class TrashTagSwiped(val tag: Tag): LabelsTabAction
    /*
    PERSON
     */
    data class EditPersonClicked(val person: Person): LabelsTabAction
    data class AcceptPersonEditionClicked(val person: Person): LabelsTabAction
    object AddNewPersonClicked: LabelsTabAction
    data class AcceptAddNewPersonClicked(val person: Person): LabelsTabAction
    object DismissPersonDialog: LabelsTabAction
    data class TrashPersonSwiped(val person: Person): LabelsTabAction
    /*
    PLACE
     */
    data class EditPlaceClicked(val place: Place): LabelsTabAction
    data class AcceptPlaceEditionClicked(val place: Place): LabelsTabAction
    object AddNewPlaceClicked: LabelsTabAction
    data class AcceptAddNewPlaceClicked(val place: Place): LabelsTabAction
    object DismissPlaceDialog: LabelsTabAction
    data class TrashPlaceSwiped(val place: Place): LabelsTabAction
}
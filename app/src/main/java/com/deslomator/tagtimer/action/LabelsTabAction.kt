package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Lbl

sealed interface LabelsTabAction {
    /*
    TAG
     */
    data class EditTagClicked(val tag: Lbl.Tag): LabelsTabAction
    data class AcceptTagEditionClicked(val tag: Lbl.Tag): LabelsTabAction
    object AddNewTagClicked: LabelsTabAction
    object DismissTagDialog: LabelsTabAction
    data class TrashTagSwiped(val tag: Lbl.Tag): LabelsTabAction
    /*
    PERSON
     */
    data class EditPersonClicked(val person: Lbl.Person): LabelsTabAction
    data class AcceptPersonEditionClicked(val person: Lbl.Person): LabelsTabAction
    object AddNewPersonClicked: LabelsTabAction
    object DismissPersonDialog: LabelsTabAction
    data class TrashPersonSwiped(val person: Lbl.Person): LabelsTabAction
    /*
    PLACE
     */
    data class EditPlaceClicked(val place: Lbl.Place): LabelsTabAction
    data class AcceptPlaceEditionClicked(val place: Lbl.Place): LabelsTabAction
    object AddNewPlaceClicked: LabelsTabAction
    object DismissPlaceDialog: LabelsTabAction
    data class TrashPlaceSwiped(val place: Lbl.Place): LabelsTabAction
}
package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.type.LabelSort
import com.deslomator.tagtimer.model.type.LabelType

sealed interface LabelPreselectionAction {
    /*
    LABEL
     */
    data class EditLabelClicked(val label: Label): LabelPreselectionAction
    data class AcceptLabelEditionClicked(val label: Label): LabelPreselectionAction
    data class AddNewLabelClicked(val type: LabelType): LabelPreselectionAction
    data object DismissLabelDialog: LabelPreselectionAction
    data class DeleteLabelClicked(val label: Label): LabelPreselectionAction
    /*
    TAG
     */
    data class SelectTagCheckedChange(val tag: Label, val checked: Boolean): LabelPreselectionAction
    data class SortTagsClicked(val tagSort: LabelSort): LabelPreselectionAction
    /*
    PERSON
     */
    data class SelectPersonCheckedChange(val person: Label, val checked: Boolean): LabelPreselectionAction
    data class SortPersonsClicked(val personSort: LabelSort): LabelPreselectionAction
    /*
    PLACE
     */
    data class SelectPlaceCheckedChange(val place: Label, val checked: Boolean): LabelPreselectionAction
    class SortPlacesClicked(val placeSort: LabelSort): LabelPreselectionAction
}
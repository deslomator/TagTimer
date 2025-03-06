package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.type.LabelSort
import com.deslomator.tagtimer.model.type.LabelType

sealed interface LabelSelectionAction {
    /*
    LABEL
     */
    data class EditLabelClicked(val label: Label): LabelSelectionAction
    data class AcceptLabelEditionClicked(val label: Label): LabelSelectionAction
    data class AddNewLabelClicked(val type: LabelType): LabelSelectionAction
    data class ArchiveLabelClicked(val label: Label): LabelSelectionAction
    data object DismissLabelDialog: LabelSelectionAction
    data class DeleteLabelClicked(val label: Label): LabelSelectionAction
    /*
    TAG
     */
    data class SelectTagCheckedChange(val tag: Label, val checked: Boolean): LabelSelectionAction
    data class SortTagsClicked(val tagSort: LabelSort): LabelSelectionAction
    /*
    PERSON
     */
    data class SelectPersonCheckedChange(val person: Label, val checked: Boolean): LabelSelectionAction
    data class SortPersonsClicked(val personSort: LabelSort): LabelSelectionAction
    /*
    PLACE
     */
    data class SelectPlaceCheckedChange(val place: Label, val checked: Boolean): LabelSelectionAction
    class SortPlacesClicked(val placeSort: LabelSort): LabelSelectionAction

    class ShowArchivedClicked(val show: Boolean): LabelSelectionAction

}
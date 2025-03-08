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
    data object DismissLabelDialog: LabelSelectionAction

    data object ArchiveLabelClicked: LabelSelectionAction
    data object UnArchiveLabelClicked: LabelSelectionAction
    data object TrashLabelClicked: LabelSelectionAction
    data object UnTrashLabelClicked: LabelSelectionAction
    data object PurgeLabelClicked: LabelSelectionAction
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

    class ShowEnabledClicked(val show: Boolean): LabelSelectionAction
    class ShowArchivedClicked(val show: Boolean): LabelSelectionAction
    class ShowTrashedClicked(val show: Boolean): LabelSelectionAction

}
package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.type.LabelSort
import com.deslomator.tagtimer.model.type.LabelType

sealed interface LabelsTabAction {
    /*
    TAG
     */
    data class EditLabelClicked(val label: Label): LabelsTabAction
    data class AcceptLabelEditionClicked(val label: Label): LabelsTabAction
    data class AddNewLabelClicked(val type: LabelType): LabelsTabAction
    data class ArchiveLabelClicked(val label: Label): LabelsTabAction
    data object DismissLabelDialog: LabelsTabAction
    class DeleteLabelClicked(val label: Label): LabelsTabAction
    class TagSortClicked(val labelSort: LabelSort): LabelsTabAction
    class ShowArchivedClicked(val show: Boolean): LabelsTabAction
    /*
    PERSON
     */
    data class PersonSortClicked(val personSort: LabelSort): LabelsTabAction
    /*
    PLACE
     */
    data class PlaceSortClicked(val placeSort: LabelSort): LabelsTabAction
}
package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.type.LabelSort

data class LabelsTabState(
    val tags: List<Label> = emptyList(),
    val persons: List<Label> = emptyList(),
    val places: List<Label> = emptyList(),
    val tagSort: LabelSort = LabelSort.COLOR,
    val personSort: LabelSort = LabelSort.NAME,
    val placeSort: LabelSort = LabelSort.NAME,

    val currentTag: Label = Label(),
    val currentPerson: Label = Label(),
    val currentPlace: Label = Label(),
    val showTagDialog: Boolean = false,
    val isEditingTag: Boolean = false,
    val isAddingNewTag: Boolean = false,
    val showPersonDialog: Boolean = false,
    val isEditingPerson: Boolean = false,
    val isAddingNewPerson: Boolean = false,
    val showPlaceDialog: Boolean = false,
    val isEditingPlace: Boolean = false,
    val isAddingNewPlace: Boolean = false,
    val canBeDeleted: Boolean = false
)

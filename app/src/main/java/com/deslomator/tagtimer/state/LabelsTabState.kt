package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.type.Sort

data class LabelsTabState(
    val tags: List<Label.Tag> = emptyList(),
    val persons: List<Label.Person> = emptyList(),
    val places: List<Label.Place> = emptyList(),
    val currentTag: Label.Tag = Label.Tag(),
    val currentPerson: Label.Person = Label.Person(),
    val currentPlace: Label.Place = Label.Place(),
    val showTagDialog: Boolean = false,
    val isEditingTag: Boolean = false,
    val isAddingNewTag: Boolean = false,
    val showPersonDialog: Boolean = false,
    val isEditingPerson: Boolean = false,
    val isAddingNewPerson: Boolean = false,
    val showPlaceDialog: Boolean = false,
    val isEditingPlace: Boolean = false,
    val isAddingNewPlace: Boolean = false,
    val tagSort: Sort = Sort.COLOR,
    val personSort: Sort = Sort.NAME,
    val placeSort: Sort = Sort.NAME,
)

package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Lbl

data class LabelsTabState(
    val tags: List<Lbl.Tag> = emptyList(),
    val persons: List<Lbl.Person> = emptyList(),
    val places: List<Lbl.Place> = emptyList(),
    val currentTag: Lbl.Tag = Lbl.Tag(),
    val currentPerson: Lbl.Person = Lbl.Person(),
    val currentPlace: Lbl.Place = Lbl.Place(),
    val showTagDialog: Boolean = false,
    val isEditingTag: Boolean = false,
    val isAddingNewTag: Boolean = false,
    val showPersonDialog: Boolean = false,
    val isEditingPerson: Boolean = false,
    val isAddingNewPerson: Boolean = false,
    val showPlaceDialog: Boolean = false,
    val isEditingPlace: Boolean = false,
    val isAddingNewPlace: Boolean = false,
)

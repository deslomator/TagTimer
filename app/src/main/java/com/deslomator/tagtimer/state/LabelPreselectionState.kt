package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Session

data class LabelPreselectionState(
    val preSelectedPersons: List<Label> = emptyList(),
    val preSelectedPlaces: List<Label> = emptyList(),
    val preSelectedTags: List<Label> = emptyList(),
    val tags: List<Label> = emptyList(),
    val persons: List<Label> = emptyList(),
    val places: List<Label> = emptyList(),
    val currentSession: Session = Session(),
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
)

package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Preselected
import com.deslomator.tagtimer.model.Session

data class LabelPreselectionState(
    val preSelectedPersons: List<Preselected.Person> = emptyList(),
    val preSelectedPlaces: List<Preselected.Place> = emptyList(),
    val preSelectedTags: List<Preselected.Tag> = emptyList(),
    val tags: List<Label.Tag> = emptyList(),
    val persons: List<Label.Person> = emptyList(),
    val places: List<Label.Place> = emptyList(),
    val currentSession: Session = Session(),
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
)

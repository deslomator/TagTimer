package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.type.Label
import com.deslomator.tagtimer.model.Person
import com.deslomator.tagtimer.model.Place
import com.deslomator.tagtimer.model.Tag

data class LabelsTabState(
    val tags: List<Tag> = emptyList(),
    val persons: List<Person> = emptyList(),
    val places: List<Place> = emptyList(),
    val currentTag: Tag = Tag(),
    val currentPerson: Person = Person(),
    val currentPlace: Place = Place(),
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

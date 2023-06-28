package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Person
import com.deslomator.tagtimer.model.Place
import com.deslomator.tagtimer.model.Tag
import com.deslomator.tagtimer.model.Trash

data class LabelsTabState(
    val currentLabel: Label = Label.TAG,
    val tags: List<Tag> = emptyList(),
    val persons: List<Person> = emptyList(),
    val places: List<Place> = emptyList(),
    val currentTag: Tag = Tag(),
    val currentPerson: Person = Person(),
    val currentPlace: Place = Place(),
//    val tagCategory: String = "",
//    val tagLabel: String = "",
//    val tagColor: Int = 0,
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

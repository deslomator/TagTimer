package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Person
import com.deslomator.tagtimer.model.Place
import com.deslomator.tagtimer.model.Tag

data class LabelsTabState(
    val tags: List<Tag> = emptyList(),
    val persons: List<Person> = emptyList(),
    val places: List<Place> = emptyList(),
    val currentTag: Tag = Tag(),
    val tagCategory: String = "",
    val tagLabel: String = "",
    val tagColor: Int = 0,
    val showTagDialog: Boolean = false,
    val isEditingTag: Boolean = false,
    val isAddingNewTag: Boolean = false,
)

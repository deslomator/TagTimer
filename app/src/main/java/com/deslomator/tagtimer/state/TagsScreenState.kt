package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Tag

data class TagsScreenState(
    val tags: List<Tag> = emptyList(),
    val currentTag: Tag = Tag(),
    val tagCategory: String = "",
    val tagLabel: String = "",
    val tagColor: Int = 0,
    val showTagDialog: Boolean = false,
    val isEditingTag: Boolean = false,
    val isAddingNewTag: Boolean = false,
    val showSnackbar: Boolean = false,
)

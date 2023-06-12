package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Tag

data class TagState(
    val tags: List<Tag> = emptyList(),
    val group: String,
    val label: String,
    val color: Long,
    val isEditingTag: Boolean = false
)

package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Tag

data class TagState(
    val tags: List<Tag> = emptyList(),
    val editingIndex: Int = 0,
    val category: String = "",
    val label: String = "",
    val color: Long = 0,
    val isEditingTag: Boolean = false,
    val showDeleteDialog: Boolean = false
)

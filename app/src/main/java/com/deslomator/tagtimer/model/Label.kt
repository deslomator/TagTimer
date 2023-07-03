package com.deslomator.tagtimer.model

import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.deslomator.tagtimer.ui.theme.colorPickerColors

sealed class Label (
    open val name: String = "",
    open val color: Int = 0,
    open val inTrash: Boolean = false,
    open val id: Int = 0
) {
    @Entity(tableName = "tags")
    data class Tag(
        override val name: String = "",
        override val color: Int = colorPickerColors[7].toArgb(),
        override val inTrash: Boolean = false,
        @PrimaryKey(autoGenerate = true) override val id: Int = 0
    ) : Label(name, color, inTrash, id)

    @Entity(tableName = "places")
    data class Place(
        override val name: String = "",
        override val color: Int = colorPickerColors[7].toArgb(),
        override val inTrash: Boolean = false,
        @PrimaryKey(autoGenerate = true) override val id: Int = 0
    ) : Label(name, color, inTrash, id)

    @Entity(tableName = "persons")
    data class Person(
        override val name: String = "",
        override val color: Int = colorPickerColors[7].toArgb(),
        override val inTrash: Boolean = false,
        @PrimaryKey(autoGenerate = true) override val id: Int = 0
    ) : Label(name, color, inTrash, id)
}


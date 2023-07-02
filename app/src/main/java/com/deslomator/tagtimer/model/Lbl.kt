package com.deslomator.tagtimer.model

import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.deslomator.tagtimer.ui.theme.colorPickerColors

sealed class Lbl (
    open val name: String = "",
    open val color: Int = 0,
    open val inTrash: Boolean = false,
    open val id: Int = 0
) {
    @Entity
    data class Tag(
        override val name: String = "",
        override val color: Int = colorPickerColors[7].toArgb(),
        override val inTrash: Boolean = false,
        @PrimaryKey(autoGenerate = true) override val id: Int = 0
    ) : Lbl(name, color, inTrash, id)

    @Entity
    data class Place(
        override val name: String = "",
        override val color: Int = colorPickerColors[7].toArgb(),
        override val inTrash: Boolean = false,
        @PrimaryKey(autoGenerate = true) override val id: Int = 0
    ) : Lbl(name, color, inTrash, id)

    @Entity
    data class Person(
        override val name: String = "",
        override val color: Int = colorPickerColors[7].toArgb(),
        override val inTrash: Boolean = false,
        @PrimaryKey(autoGenerate = true) override val id: Int = 0
    ) : Lbl(name, color, inTrash, id)
}


package com.deslomator.tagtimer.model

import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.deslomator.tagtimer.ui.theme.colorPickerColors

@Entity
data class Person(
    val color: Int = colorPickerColors[7].toArgb(),
    val inTrash: Boolean = false,
    @PrimaryKey val name: String = ""
)

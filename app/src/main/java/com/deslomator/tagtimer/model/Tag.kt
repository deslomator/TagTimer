package com.deslomator.tagtimer.model

import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.deslomator.tagtimer.ui.theme.PurpleGrey80
import com.deslomator.tagtimer.ui.theme.colorPickerColors

@Entity
data class Tag(
    val category: String = "",
    val label: String = "",
    val color: Int = colorPickerColors[7].toArgb(),
    @PrimaryKey(autoGenerate = true) val id: Int = 0
    )

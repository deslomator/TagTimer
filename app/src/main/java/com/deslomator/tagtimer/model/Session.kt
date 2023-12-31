package com.deslomator.tagtimer.model

import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.deslomator.tagtimer.ui.theme.PurpleGrey80
import com.deslomator.tagtimer.ui.theme.colorPickerColors

@Entity
data class Session(
    val lastAccessMillis: Long = 0,
    val name: String = "",
    val color: Int = colorPickerColors[7].toArgb(),
    val startTimeMillis: Long = 0,
    val endTimeMillis: Long = 0,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)

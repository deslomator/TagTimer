package com.deslomator.tagtimer.model

import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.deslomator.tagtimer.ui.theme.colorPickerColors

@Entity
data class Event(
    val sessionId: Int = 0,
    val elapsedTimeMillis: Long = 0,
    val note: String = "",
    val label: String = "",
    val person: String = "",
    val place: String = "",
    val color: Int = colorPickerColors[7].toArgb(),
    val inTrash: Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

package com.deslomator.tagtimer.model

import androidx.annotation.Keep
import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.deslomator.tagtimer.ui.theme.colorPickerColors
import kotlinx.serialization.Serializable

@Keep
@Serializable
@Entity(tableName = "sessions")
data class Session(
    val lastAccessMillis: Long = System.currentTimeMillis(),
    val name: String = "",
    val notes: String = "",
    val color: Int = colorPickerColors[7].toArgb(),
    val durationMillis: Long = 0,
    val eventCount: Int = 0,
    val inTrash: Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)

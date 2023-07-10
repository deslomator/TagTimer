package com.deslomator.tagtimer.model

import androidx.annotation.Keep
import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.deslomator.tagtimer.ui.theme.colorPickerColors
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
@Entity(tableName = "sessions")
data class Session(
    @SerialName("last_access_millis") val lastAccessMillis: Long = System.currentTimeMillis(),
    val name: String = "",
    val notes: String = "",
    val color: Int = colorPickerColors[7].toArgb(),
    @SerialName("durationMillis") val durationMillis: Long = 0,
    @SerialName("eventCount") val eventCount: Int = 0,
    @SerialName("startTimestampMillis") val startTimestampMillis: Long = 0,
    @SerialName("in_trash") val inTrash: Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
)

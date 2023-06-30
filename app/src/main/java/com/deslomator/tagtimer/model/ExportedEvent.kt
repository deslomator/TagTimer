package com.deslomator.tagtimer.model

import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.deslomator.tagtimer.ui.theme.colorPickerColors
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExportedEvent(
    val label: String = "",
    val note: String = "",
    val person: String = "",
    val place: String = "",
    @SerialName("elapsed_time_secs") val elapsedTimeSeconds: Int = 0,
) {
    constructor(event: Event): this(
        event.label,
        event.note,
        event.person,
        event.place,
        (event.elapsedTimeMillis / 1000).toInt()
    )
}

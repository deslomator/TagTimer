package com.deslomator.tagtimer.model

import androidx.annotation.Keep
import androidx.compose.ui.graphics.toArgb
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.deslomator.tagtimer.ui.theme.colorPickerColors
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
@Entity(tableName = "sessions")
data class Session(
    @SerialName("last_access_millis")
    @ColumnInfo(name = "last_access_millis")
    val lastAccessMillis: Long = System.currentTimeMillis(),

    val name: String = "",
    val notes: String = "",
    val color: Int = colorPickerColors[7].toArgb(),

    @SerialName("duration_millis")
    @ColumnInfo(name = "duration_millis")
    val durationMillis: Long = 0,

    @SerialName("event_count")
    @ColumnInfo(name = "event_count")
    val eventCount: Int = 0,

    @SerialName("start_timestamp_millis")
    @ColumnInfo(name = "start_timestamp_millis")
    val startTimestampMillis: Long = 0,

    @SerialName("in_trash")
    @ColumnInfo(name = "in_trash")
    val inTrash: Boolean = false,

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
)

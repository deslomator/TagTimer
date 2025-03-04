package com.deslomator.tagtimer.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.deslomator.tagtimer.ui.theme.colorPickerColors
import com.deslomator.tagtimer.ui.theme.toHex
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
@Entity(tableName = "events")
data class Event(
    @SerialName("session_id")
    @ColumnInfo(name = "session_id")
    val sessionId: Long = 0,

    @SerialName("elapsed_time_millis")
    @ColumnInfo(name = "elapsed_time_millis")
    val elapsedTimeMillis: Long = 0,

    val note: String = "",
    val color: String = colorPickerColors[7].toHex(),

    val tagId: Long? = null,
    val personId: Long? = null,
    val placeId: Long? = null,

    @SerialName("in_trash")
    @ColumnInfo(name = "in_trash")
    val inTrash: Boolean = false,

    @PrimaryKey(autoGenerate = true) val id: Long? = null,
) {
    @delegate:Ignore
    val longColor: Long by lazy { color.toLong(16) }
}



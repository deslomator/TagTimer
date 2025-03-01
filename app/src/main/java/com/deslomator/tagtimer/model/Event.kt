package com.deslomator.tagtimer.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.Relation
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

//    val labelIds: List<Long> = emptyList(),

    @SerialName("in_trash")
    @ColumnInfo(name = "in_trash")
    val inTrash: Boolean = false,

    @PrimaryKey(autoGenerate = true) val id: Long? = null,
) {
    @delegate:Ignore
    val longColor: Long by lazy { color.toLong(16) }
}

data class EventForDisplay(
    @Embedded val event: Event = Event(),
    @Relation(
        parentColumn = "tagId",
        entityColumn = "id"
    )
    val tags: List<Label> = emptyList(),

    @Relation(
        parentColumn = "personId",
        entityColumn = "id"
    )
    val persons: List<Label> = emptyList(),

    @Relation(
        parentColumn = "placeId",
        entityColumn = "id"
    )
    val places: List<Label> = emptyList()
) {
    fun getTagName() = tags.firstOrNull()?.name
    fun getPersonName() = persons.firstOrNull()?.name
    fun getPlaceName() = places.firstOrNull()?.name
}



package com.deslomator.tagtimer.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
@Entity(
    tableName = "selected",
    primaryKeys = ["session_id", "label_id"],
    foreignKeys = [
        ForeignKey(
            entity = Session::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("session_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = Label::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("label_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class Selected(
    @SerialName("session_id")
    @ColumnInfo(name = "session_id")
    val sessionId: Long = 0,

    @SerialName("label_id")
    @ColumnInfo(name = "label_id")
    val labelId: Long = 0,
)
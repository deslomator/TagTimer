package com.deslomator.tagtimer.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
@Entity(
    tableName = "preselected",
    primaryKeys = ["session_id", "label_id"]
)
data class Preselected(
    @SerialName("session_id")
    @ColumnInfo(name = "session_id")
    val sessionId: Long = 0,

    @SerialName("label_id")
    @ColumnInfo(name = "label_id")
    val labelId: Long = 0,
)
package com.deslomator.tagtimer.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
sealed class Preselected {
    abstract val sessionId: Long
    abstract val labelId: String

    @Keep
    @Serializable
    @Entity(
        tableName = "ps_persons",
        primaryKeys = ["session_id", "person_id"]
    )
    data class Person(
        @SerialName("session_id")
        @ColumnInfo(name = "session_id")
        override val sessionId: Long = 0,

        @SerialName("person_id")
        @ColumnInfo(name = "person_id")
        override val labelId: String = "",
    ) : Preselected()

    @Keep
    @Serializable
    @Entity(
        tableName = "ps_places",
        primaryKeys = ["session_id", "place_id"]
    )
    data class Place(
        @SerialName("session_id")
        @ColumnInfo(name = "session_id")
        override val sessionId: Long = 0,

        @SerialName("place_id")
        @ColumnInfo(name = "place_id")
        override val labelId: String = ""
    ) : Preselected()

    @Keep
    @Serializable
    @Entity(
        tableName = "ps_tags",
        primaryKeys = ["session_id", "tag_id"]
    )
    data class Tag(
        @SerialName("session_id")
        @ColumnInfo(name = "session_id")
        override val sessionId: Long = 0,

        @SerialName("tag_id")
        @ColumnInfo(name = "tag_id")
        override val labelId: String = ""
    ) : Preselected()
}


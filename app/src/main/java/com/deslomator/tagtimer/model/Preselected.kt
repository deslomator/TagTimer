package com.deslomator.tagtimer.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
sealed class Preselected (
    @SerialName("super_sessionId") open val sessionId: Long = 0,
    @SerialName("super_labelId") open val labelId: Long = 0,
    @SerialName("super_id") open  val id: Long = 0
) {
    @Keep
    @Serializable
    @Entity(tableName = "ps_persons")
    data class Person(
        @SerialName("session_id") override val sessionId: Long = 0,
        @SerialName("person_id") override val labelId: Long = 0,
        @PrimaryKey(autoGenerate = true) override val id: Long = 0
    ) : Preselected(sessionId, labelId, id)

    @Keep
    @Serializable
    @Entity(tableName = "ps_places")
    data class Place(
        @SerialName("session_id") override val sessionId: Long = 0,
        @SerialName("place_id") override val labelId: Long = 0,
        @PrimaryKey(autoGenerate = true) override val id: Long = 0
    ) : Preselected(sessionId, labelId, id)

    @Keep
    @Serializable
    @Entity(tableName = "ps_tags")
    data class Tag(
        @SerialName("session_id") override val sessionId: Long = 0,
        @SerialName("tag_id") override val labelId: Long = 0,
        @PrimaryKey(autoGenerate = true) override val id: Long = 0
    ) : Preselected(sessionId, labelId, id)
}


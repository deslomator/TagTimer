package com.deslomator.tagtimer.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
sealed class Preselected (
    @SerialName("super_sessionId") open val sessionId: Int = 0,
    @SerialName("super_labelId") open val labelId: Int = 0,
    @SerialName("super_id") open  val id: Int = 0
) {
    @Keep
    @Serializable
    @Entity(tableName = "ps_persons")
    data class Person(
        override val sessionId: Int = 0,
        override val labelId: Int = 0,
        @PrimaryKey(autoGenerate = true) override val id: Int = 0
    ) : Preselected(sessionId, labelId, id)

    @Keep
    @Serializable
    @Entity(tableName = "ps_places")
    data class Place(
        override val sessionId: Int = 0,
        override val labelId: Int = 0,
        @PrimaryKey(autoGenerate = true) override val id: Int = 0
    ) : Preselected(sessionId, labelId, id)

    @Keep
    @Serializable
    @Entity(tableName = "ps_tags")
    data class Tag(
        override val sessionId: Int = 0,
        override val labelId: Int = 0,
        @PrimaryKey(autoGenerate = true) override val id: Int = 0
    ) : Preselected(sessionId, labelId, id)
}


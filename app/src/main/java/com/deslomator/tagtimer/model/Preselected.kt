package com.deslomator.tagtimer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

sealed class Preselected (
    open val sessionId: Int = 0,
    open val labelId: Int = 0,
    open  val id: Int = 0
) {
    @Entity(tableName = "ps_persons")
    data class Person(
        override val sessionId: Int = 0,
        override val labelId: Int = 0,
        @PrimaryKey(autoGenerate = true) override val id: Int = 0
    ) : Preselected(sessionId, labelId, id)

    @Entity(tableName = "ps_places")
    data class Place(
        override val sessionId: Int = 0,
        override val labelId: Int = 0,
        @PrimaryKey(autoGenerate = true) override val id: Int = 0
    ) : Preselected(sessionId, labelId, id)

    @Entity(tableName = "ps_tags")
    data class Tag(
        override val sessionId: Int = 0,
        override val labelId: Int = 0,
        @PrimaryKey(autoGenerate = true) override val id: Int = 0
    ) : Preselected(sessionId, labelId, id)
}


package com.deslomator.tagtimer.model

import androidx.room.Embedded
import androidx.room.Relation

data class EventForDisplay(
    @Embedded val event: Event = Event(),

    @Relation(
        parentColumn = "tagId",
        entityColumn = "id"
    )
    val tag: Label? = null,

    @Relation(
        parentColumn = "personId",
        entityColumn = "id"
    )
    val person: Label? = null,

    @Relation(
        parentColumn = "placeId",
        entityColumn = "id"
    )
    val place: Label? = null,
)
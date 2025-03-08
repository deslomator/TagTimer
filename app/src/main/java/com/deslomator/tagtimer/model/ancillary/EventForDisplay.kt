package com.deslomator.tagtimer.model.ancillary

import androidx.room.Embedded
import androidx.room.Relation
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Label

data class EventForDisplay(
    @Embedded val event: Event = Event(),

    @Relation(
        parentColumn = "tag_id",
        entityColumn = "id"
    )
    val tag: Label? = null,

    @Relation(
        parentColumn = "person_id",
        entityColumn = "id"
    )
    val person: Label? = null,

    @Relation(
        parentColumn = "place_id",
        entityColumn = "id"
    )
    val place: Label? = null,
)
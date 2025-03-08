package com.deslomator.tagtimer.model.ancillary

import androidx.room.Embedded
import androidx.room.Relation
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Label

data class LabelForDisplay(

    @Embedded
    val label: Label,

    @Relation(
        parentColumn = "id",
        entityColumn = "tag_id"
    )
    val tags: List<Event> = emptyList(),

    @Relation(
        parentColumn = "id",
        entityColumn = "person_id"
    )
    val persons: List<Event> = emptyList(),

    @Relation(
        parentColumn = "id",
        entityColumn = "place_id"
    )
    val places: List<Event> = emptyList(),
) {
    val isUsed by lazy { (tags.size + persons.size + places.size) > 0 }
}
package com.deslomator.tagtimer.model

import androidx.annotation.Keep
import androidx.room.Embedded
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class FullBackup(
    @Embedded val persons: List<Label.Person> = emptyList(),
    @Embedded val places: List<Label.Place> = emptyList(),
    @Embedded val tags: List<Label.Tag> = emptyList(),
    @Embedded val preselectedPersons: List<Preselected.Person> = emptyList(),
    @Embedded val preselectedPlaces: List<Preselected.Place> = emptyList(),
    @Embedded val preselectedTags: List<Preselected.Tag> = emptyList(),
    @Embedded val sessions: List<Session> = emptyList(),
    @Embedded val events: List<Event> = emptyList(),
)

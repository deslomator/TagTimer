package com.deslomator.tagtimer.model

import androidx.annotation.Keep
import androidx.room.Embedded
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class DbBackup(
    @Embedded
    val persons: List<Label.Person> = emptyList(),
    @Embedded
    val places: List<Label.Place> = emptyList(),
    @Embedded
    val tags: List<Label.Tag> = emptyList(),
    @Embedded
    @SerialName("preselected_persons")
    val preselectedPersons: List<Preselected.Person> = emptyList(),
    @Embedded
    @SerialName("preselected_places")
    val preselectedPlaces: List<Preselected.Place> = emptyList(),
    @Embedded
    @SerialName("preselected_tags")
    val preselectedTags: List<Preselected.Tag> = emptyList(),
    @Embedded
    val sessions: List<Session> = emptyList(),
    @Embedded
    val events: List<Event> = emptyList(),
    @Embedded
    val prefs: List<Preference> = emptyList(),
){
    fun isEmpty(): Boolean {
        return this.tags.isEmpty() &&
                this.places.isEmpty() &&
                this.persons.isEmpty() &&
                this.events.isEmpty() &&
                this.preselectedPersons.isEmpty() &&
                this.preselectedPlaces.isEmpty() &&
                this.preselectedTags.isEmpty() &&
                this.sessions.isEmpty() &&
                this.prefs.isEmpty()
    }

    fun isLabelsOnly(): Boolean {
        return (this.tags.isNotEmpty() ||
                this.places.isNotEmpty() ||
                this.persons.isNotEmpty()) &&
                this.events.isEmpty() &&
                this.preselectedPersons.isEmpty() &&
                this.preselectedPlaces.isEmpty() &&
                this.preselectedTags.isEmpty() &&
                this.sessions.isEmpty() &&
                this.prefs.isEmpty()
    }
}

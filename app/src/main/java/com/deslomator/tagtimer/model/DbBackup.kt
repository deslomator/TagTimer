package com.deslomator.tagtimer.model

import androidx.annotation.Keep
import androidx.room.Embedded
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class DbBackup(
    @Embedded
    val labels: List<Label> = emptyList(),
    @Embedded
    val preselected: List<Preselected> = emptyList(),
    @Embedded
    val sessions: List<Session> = emptyList(),
    @Embedded
    val events: List<Event> = emptyList(),
    @Embedded
    val prefs: List<Preference> = emptyList(),
) {
    fun isEmpty(): Boolean {
        return this.labels.isEmpty() &&
                this.events.isEmpty() &&
                this.preselected.isEmpty() &&
                this.sessions.isEmpty() &&
                this.prefs.isEmpty()
    }

    fun isLabelsOnly(): Boolean {
        return this.labels.isNotEmpty() &&
                this.events.isEmpty() &&
                this.preselected.isEmpty() &&
                this.sessions.isEmpty() &&
                this.prefs.isEmpty()
    }
}

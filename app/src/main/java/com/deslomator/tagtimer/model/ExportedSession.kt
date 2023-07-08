package com.deslomator.tagtimer.model

import androidx.annotation.Keep
import com.deslomator.tagtimer.util.toDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class ExportedSession(
    val date: String = "",
    val name: String = "",
    val notes: String = "",
    @SerialName("duration_secs") val durationSecs: Int = 0,
    val events: List<ExportedEvent> = emptyList()
) {
    constructor(session: Session, events: List<Event>): this(
        date = session.lastAccessMillis.toDateTime(),
        name = session.name,
        notes = session.notes,
        durationSecs = (session.durationMillis / 1000).toInt(),
        events = events.map { ExportedEvent(it) }
    )
}

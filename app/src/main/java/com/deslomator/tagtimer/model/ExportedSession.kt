package com.deslomator.tagtimer.model

import com.deslomator.tagtimer.toDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExportedSession(
    val date: String = "",
    val name: String = "",
    val notes: String = "",
    @SerialName("duration_secs") val durationSecs: Int = 0,
    val events: List<ExportedEvent> = emptyList()
) {
    constructor(session: Session, events: List<Event>): this(
        session.lastAccessMillis.toDateTime(),
        session.name,
        session.notes,
        (session.durationMillis / 1000).toInt(),
        events.map { ExportedEvent(it) }
    )
}

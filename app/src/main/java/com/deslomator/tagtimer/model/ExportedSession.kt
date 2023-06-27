package com.deslomator.tagtimer.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExportedSession(
    val date: String = "",
    val name: String = "",
    val notes: String = "",
    @SerialName("duration_secs") val durationSecs: Int = 0,
    val events: List<ExportedEvent> = emptyList()
)

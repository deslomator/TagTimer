package com.deslomator.tagtimer.model

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class ExportedEvent(
    val label: String = "",
    val note: String = "",
    val person: String = "",
    val place: String = "",
    @SerialName("elapsed_time_secs") val elapsedTimeSeconds: Int = 0,
) {
    constructor(event: Event): this(
        label = event.tag,
        note = event.note,
        person = event.person,
        place = event.place,
        elapsedTimeSeconds = (event.elapsedTimeMillis / 1000).toInt()
    )
}

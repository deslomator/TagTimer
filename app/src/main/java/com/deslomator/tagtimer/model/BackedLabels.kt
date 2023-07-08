package com.deslomator.tagtimer.model

import androidx.annotation.Keep
import androidx.room.Embedded
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class BackedLabels(
    @Embedded val persons: List<Label.Person> = emptyList(),
    @Embedded val places: List<Label.Place> = emptyList(),
    @Embedded val tags: List<Label.Tag> = emptyList(),
)

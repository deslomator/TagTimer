package com.deslomator.tagtimer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PreSelectedPlace(
    val sessionId: Int = 0,
    val placeId: Int = 0,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

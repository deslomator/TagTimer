package com.deslomator.tagtimer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Event(
    val sessionId: Int,
    val tagId: Int,
    val timestampMillis: Long,
    val note: String = "",
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

package com.deslomator.tagtimer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Event(
    val sessionId: Int = 0,
    val tagId: Int = 0,
    val timestampMillis: Long = 0,
    val note: String = "",
    val inTrash: Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

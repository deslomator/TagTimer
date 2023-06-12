package com.deslomator.tagtimer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val sessionId: Long,
    val timestamp: Long,
    val group: String,
    val label: String,
    val color: Long,
    val notes: String
)

package com.deslomator.tagtimer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sessionId: Int = 0,
    val timestampMillis: Long = 0,
    val category: String = "",
    val label: String = "",
    val color: Long = 0,
    val note: String = ""
)

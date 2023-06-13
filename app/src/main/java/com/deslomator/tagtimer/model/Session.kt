package com.deslomator.tagtimer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Session(
    val lastAccessMillis: Long = 0,
    val name: String = "",
    val color: Long = 0,
    val startTimeMillis: Long = 0,
    val endTimeMillis: Long = 0,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)

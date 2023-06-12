package com.deslomator.tagtimer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Session(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val lastAccess: Long,
    val name: String,
    val color: Long,
    val startTimeMillis: Long,
    val endTimeMillis: Long
)

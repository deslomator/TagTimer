package com.deslomator.tagtimer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Session(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var lastAccess: Long,
    var name: String,
    var color: Long,
    var startTimeMillis: Long,
    var endTimeMillis: Long
)

package com.deslomator.tagtimer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UsedTag(
    val sessionId: Int,
    val tagId: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

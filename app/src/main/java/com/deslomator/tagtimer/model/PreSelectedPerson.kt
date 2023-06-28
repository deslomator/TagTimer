package com.deslomator.tagtimer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PreSelectedPerson(
    val sessionId: Int = 0,
    val personId: Int = 0,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

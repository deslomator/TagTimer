package com.deslomator.tagtimer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tag(
    val category: String,
    val label: String,
    val color: Long,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
    )

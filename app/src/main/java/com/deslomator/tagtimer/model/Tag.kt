package com.deslomator.tagtimer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tag(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val category: String = "",
    val label: String = "",
    val color: Long = 0
    )

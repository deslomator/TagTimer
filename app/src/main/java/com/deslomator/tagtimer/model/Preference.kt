package com.deslomator.tagtimer.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.deslomator.tagtimer.model.type.Sort
import kotlinx.serialization.Serializable

@Keep
@Serializable
@Entity(tableName = "preferences")
class Preference(
    val value: String,
    @PrimaryKey val sKey: String,
) {
    fun getSort(): Sort? = when (value) {
        Sort.COLOR.name -> Sort.COLOR
        Sort.NAME.name -> Sort.NAME
        else -> null

    }
}
package com.deslomator.tagtimer.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.deslomator.tagtimer.model.type.LabelSort
import com.deslomator.tagtimer.model.type.SessionSort
import kotlinx.serialization.Serializable

@Keep
@Serializable
@Entity(tableName = "preferences")
class Preference(
    @PrimaryKey val key: String,
    val value: String
) {
    fun getLabelSort(): LabelSort? = when (value) {
        LabelSort.COLOR.name -> LabelSort.COLOR
        LabelSort.NAME.name -> LabelSort.NAME
        else -> null
    }
    fun getSessionSort(): SessionSort? = when (value) {
        SessionSort.DATE.name -> SessionSort.DATE
        SessionSort.LAST_ACCESS.name -> SessionSort.LAST_ACCESS
        SessionSort.NAME.name -> SessionSort.NAME
        else -> null
    }
}
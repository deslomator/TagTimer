package com.deslomator.tagtimer.dao

import androidx.room.TypeConverter
import com.deslomator.tagtimer.model.type.PrefKey

class Converters {

    @TypeConverter
    fun stringToPrefKey(value: String): PrefKey = PrefKey.valueOf(value)

}
package com.deslomator.tagtimer.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Preference
import com.deslomator.tagtimer.model.Selected
import com.deslomator.tagtimer.model.Session

@Database(
    entities = [
        Event::class,
        Session::class,
        Label::class,
        Selected::class,
        Preference::class,
    ],
    version = 1,
    exportSchema = false,
)
//@TypeConverters(
//    Converters::class
//)
abstract class SessionsDatabase: RoomDatabase() {

    abstract val appDao: AppDao
}

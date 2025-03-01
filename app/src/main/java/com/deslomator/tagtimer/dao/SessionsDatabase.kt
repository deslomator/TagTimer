package com.deslomator.tagtimer.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Preference
import com.deslomator.tagtimer.model.Preselected
import com.deslomator.tagtimer.model.Session

@Database(
    entities = [
        Event::class,
        Session::class,
        Label::class,
        Preselected::class,
        Preference::class,
    ],
    version = 1
)
abstract class SessionsDatabase: RoomDatabase() {

    abstract val appDao: AppDao
}
package com.deslomator.tagtimer.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Lbl
import com.deslomator.tagtimer.model.PreSelectedPerson
import com.deslomator.tagtimer.model.PreSelectedPlace
import com.deslomator.tagtimer.model.PreSelectedTag
import com.deslomator.tagtimer.model.Session

@Database(
    entities = [
        Event::class,
        Session::class,
        Lbl.Tag::class,
        Lbl.Person::class,
        Lbl.Place::class,
        PreSelectedTag::class,
        PreSelectedPerson::class,
        PreSelectedPlace::class
    ],
    version = 1
)
abstract class SessionsDatabase: RoomDatabase() {

    abstract val appDao: AppDao
}
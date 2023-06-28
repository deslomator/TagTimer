package com.deslomator.tagtimer.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Person
import com.deslomator.tagtimer.model.Place
import com.deslomator.tagtimer.model.PreSelectedPerson
import com.deslomator.tagtimer.model.PreSelectedPlace
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.Tag
import com.deslomator.tagtimer.model.PreSelectedTag

@Database(
    entities = [
        Event::class,
        Session::class,
        Tag::class,
        Person::class,
        Place::class,
        PreSelectedTag::class,
        PreSelectedPerson::class,
        PreSelectedPlace::class
    ],
    version = 1
)
abstract class SessionsDatabase: RoomDatabase() {

    abstract val appDao: AppDao
}
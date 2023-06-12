package com.deslomator.tagtimer.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.Tag

@Database(
    entities = [Event::class, Session::class, Tag::class],
    version = 1
)
abstract class SessionDatabase: RoomDatabase() {

    abstract val eventDao: EventDao
    abstract val sessionDao: SessionDao
    abstract val tagDao: TagDao
}
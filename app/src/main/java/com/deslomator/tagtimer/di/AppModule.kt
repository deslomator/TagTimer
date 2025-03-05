package com.deslomator.tagtimer.di

import android.content.Context
import androidx.room.Room
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.dao.SessionsDatabase

const val DATABASE_NAME = "sessions.db"

interface AppModule {
    val appContext: Context
    val appDao: AppDao
}

class AppModuleImpl(
    private val context: Context
) : AppModule {

    override val appContext = context

    override val appDao: AppDao by lazy {
        Room.databaseBuilder(
            context,
            SessionsDatabase::class.java,
            DATABASE_NAME
        ).build().appDao
    }
}
package com.deslomator.tagtimer.di

import android.content.Context
import androidx.room.Room
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.dao.SessionsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDao(@ApplicationContext appContext: Context): AppDao =
        Room.databaseBuilder(
            appContext,
            SessionsDatabase::class.java,
            DATABASE_NAME
        ).build().appDao

    /*@Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): SessionsDatabase =
        Room.databaseBuilder(
            appContext,
            SessionsDatabase::class.java,
            DATABASE_NAME
        ).build()*/

    /*@Singleton
    @Provides
    fun provideActiveSessionViewModel(dao: AppDao) :ActiveSessionViewModel {

        return viewModelFactory {
            initializer {
                ActiveSessionViewModel(dao)
            }
        }.create(ActiveSessionViewModel::class.java)
    }*/
}

const val DATABASE_NAME = "sessions.db"

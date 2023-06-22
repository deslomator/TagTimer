package com.deslomator.tagtimer.di

import android.content.Context
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Room
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.dao.SessionsDatabase
import com.deslomator.tagtimer.viewmodel.ActiveSessionViewModel
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
    fun provideActiveSessionViewModel(dao: AppDao) :ActiveSessionViewModel {

        return viewModelFactory {
            initializer {
                ActiveSessionViewModel(dao)
            }
        }.create(ActiveSessionViewModel::class.java)
    }*/
}

private const val DATABASE_NAME = "sessions.db"

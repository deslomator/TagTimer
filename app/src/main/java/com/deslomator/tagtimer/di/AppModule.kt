package com.deslomator.tagtimer.di

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.dao.SessionsDatabase
import com.deslomator.tagtimer.viewmodel.AppViewModel
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
            "sessions.db"
        ).build().appDao
}
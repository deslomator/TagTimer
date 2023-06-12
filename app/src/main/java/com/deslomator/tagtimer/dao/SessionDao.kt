package com.deslomator.tagtimer.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.deslomator.tagtimer.model.Session
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {

    @Upsert
    suspend fun upsertSession(sessionId: Int)

    @Delete
    suspend fun deleteSession(sessionId: Int) {

    }

    @Query("SELECT * FROM session WHERE id = :sessionId")
    fun getSession(sessionId: Int): Flow<Session>

    @Query("SELECT * FROM session ORDER BY lastAccess DESC")
    fun getSessions(): Flow<List<Session>>


}
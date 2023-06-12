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
    suspend fun upsertSession(session: Session)

    @Delete
    suspend fun deleteSession(session: Session)

    @Query("SELECT * FROM session WHERE id = :sessionId")
    fun getSession(sessionId: Int): Flow<Session>

    @Query("SELECT * FROM session ORDER BY lastAccessMillis DESC")
    fun getSessions(): Flow<List<Session>>

}
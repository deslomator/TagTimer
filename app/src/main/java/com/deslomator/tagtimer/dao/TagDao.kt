package com.deslomator.tagtimer.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.deslomator.tagtimer.model.Tag
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {

    @Upsert
    suspend fun upsertTag(tag: Tag)

    @Delete
    suspend fun deleteTag(tag: Tag)

    @Query("SELECT * FROM tag ORDER BY category, label ASC")
    fun getTags(): Flow<List<Tag>>
}
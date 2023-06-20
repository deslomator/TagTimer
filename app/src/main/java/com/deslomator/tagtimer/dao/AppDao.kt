package com.deslomator.tagtimer.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.Tag
import com.deslomator.tagtimer.model.UsedTag
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Upsert
    suspend fun upsertEvent(event: Event)

    @Delete
    suspend fun deleteEvent(event: Event)

    @Query("DELETE FROM event WHERE sessionId = :sessionId")
    suspend fun deleteEventsForSession(sessionId: Int)

    @Query("SELECT * FROM event WHERE sessionId = :sessionId AND inTrash = 0 ORDER BY timestampMillis ASC")
    fun getActiveEventsForSession(sessionId: Int): Flow<List<Event>>

    @Query("SELECT * FROM event WHERE sessionId = :sessionId AND inTrash = 1 ORDER BY timestampMillis ASC")
    fun getTrashedEventsForSession(sessionId: Int): Flow<List<Event>>

    @Query("SELECT DISTINCT category FROM tag ORDER BY category ASC")
    fun getCategories(): Flow<List<String>>

    @Upsert
    suspend fun upsertSession(session: Session)

    @Delete
    suspend fun deleteSession(session: Session)

    @Query("SELECT * FROM session ORDER BY lastAccessMillis DESC")
    fun getSessions(): Flow<List<Session>>

    @Query("SELECT * FROM session WHERE inTrash = 0 ORDER BY lastAccessMillis DESC")
    fun getActiveSessions(): Flow<List<Session>>

    @Query("SELECT * FROM session WHERE inTrash = 1 ORDER BY lastAccessMillis DESC")
    fun getTrashedSessions(): Flow<List<Session>>

    @Upsert
    suspend fun upsertTag(tag: Tag)

    @Delete
    suspend fun deleteTag(tag: Tag)

    @Query("SELECT * FROM tag ORDER BY category, label ASC")
    fun getTags(): Flow<List<Tag>>
    @Query("SELECT * FROM tag WHERE inTrash = 0 ORDER BY label,category ASC")
    fun getActiveTags(): Flow<List<Tag>>
    @Query("SELECT * FROM tag WHERE inTrash = 1 ORDER BY label,category ASC")
    fun getTrashedTags(): Flow<List<Tag>>

    @Upsert
    suspend fun upsertUsedTag(usedTag: UsedTag)

    @Delete
    suspend fun deleteUsedTag(usedTag: UsedTag)

    @Query("SELECT usedtag.sessionId AS sessionId, usedtag.tagId AS tagId, usedtag.id AS id " +
            "FROM usedtag INNER JOIN tag on usedtag.tagId = tag.id " +
            "WHERE usedtag.sessionId = :sessionId " +
            "ORDER BY tag.category, tag.label ASC")
    fun getUsedTagsForSession(sessionId: Int): Flow<List<UsedTag>>

    @Query("DELETE FROM event " +
            "WHERE NOT EXISTS (SELECT NULL FROM session WHERE event.sessionId = session.id)")
    suspend fun clearOrphanEvents(): Int

    @Query("DELETE FROM usedtag " +
            "WHERE NOT EXISTS (SELECT NULL FROM session WHERE usedtag.sessionId = session.id)")
    suspend fun clearOrphanUsedTags(): Int

}
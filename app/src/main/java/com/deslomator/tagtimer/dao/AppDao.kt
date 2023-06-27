package com.deslomator.tagtimer.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.PreSelectedTag
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.Tag
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    /*
    EVENT
    */
    @Upsert
    suspend fun upsertEvent(event: Event)

    @Delete
    suspend fun deleteEvent(event: Event)

    @Query("DELETE FROM event WHERE sessionId = :sessionId")
    suspend fun deleteEventsForSession(sessionId: Int)

    @Query("SELECT * FROM event WHERE id = :eventId")
    suspend fun getEventById(eventId: Int): Event

    @Query("SELECT * FROM event WHERE sessionId = :sessionId AND inTrash = 0 ORDER BY elapsedTimeMillis ASC")
    fun getActiveEventsForSession(sessionId: Int): Flow<List<Event>>

    @Query("SELECT * FROM event WHERE sessionId = :sessionId AND inTrash = 1 ORDER BY elapsedTimeMillis ASC")
    fun getTrashedEventsForSession(sessionId: Int): Flow<List<Event>>
    /*
    CATEGORY
     */

    @Query("SELECT DISTINCT category FROM tag ORDER BY category ASC")
    fun getCategories(): Flow<List<String>>
    /*
    SESSION
     */
    @Upsert
    suspend fun upsertSession(session: Session)

    @Delete
    suspend fun deleteSession(session: Session)

    @Query("SELECT * FROM session WHERE id = :id")
    suspend fun getSession(id: Int): Session

    @Query("SELECT * FROM session ORDER BY lastAccessMillis DESC")
    fun getSessions(): Flow<List<Session>>

    @Query("SELECT * FROM session WHERE inTrash = 0 ORDER BY lastAccessMillis DESC")
    fun getActiveSessions(): Flow<List<Session>>

    @Query("SELECT * FROM session WHERE inTrash = 1 ORDER BY lastAccessMillis DESC")
    fun getTrashedSessions(): Flow<List<Session>>
    /*
    TAG
     */
    @Upsert
    suspend fun upsertTag(tag: Tag)
    @Delete
    suspend fun deleteTag(tag: Tag)
    @Query("SELECT * FROM tag WHERE id = :id")
    suspend fun getTag(id: Int): Tag
    @Query("SELECT * FROM tag ORDER BY category, label ASC")
    fun getTags(): Flow<List<Tag>>
    @Query("SELECT * FROM tag WHERE inTrash = 0 ORDER BY label,category ASC")
    fun getActiveTags(): Flow<List<Tag>>
    @Query("SELECT * FROM tag WHERE inTrash = 1 ORDER BY label,category ASC")
    fun getTrashedTags(): Flow<List<Tag>>
    /*
    PRESELECTED TAG
     */
    @Upsert
    suspend fun upsertPreSelectedTag(preSelectedTag: PreSelectedTag)
    @Delete
    suspend fun deletePreSelectedTag(preSelectedTag: PreSelectedTag)
    @Query("DELETE FROM preselectedtag WHERE id = :preSelectedTagId")
    suspend fun deletePreSelectedTag(preSelectedTagId: Int)
    @Query("SELECT * FROM preselectedtag WHERE sessionId = :sessionId")
    fun getPreSelectedTagsForSession(sessionId: Int): Flow<List<PreSelectedTag>>
    /*
    ORPHAN
     */
    @Query("DELETE FROM event " +
            "WHERE NOT EXISTS (SELECT NULL FROM session WHERE event.sessionId = session.id)")
    suspend fun clearOrphanEvents(): Int
    @Query("DELETE FROM preselectedtag " +
            "WHERE NOT EXISTS (SELECT NULL FROM session WHERE preselectedtag.sessionId = session.id)")
    suspend fun clearOrphanPreSelectedTags(): Int

}

private const val TAG ="AppDao"
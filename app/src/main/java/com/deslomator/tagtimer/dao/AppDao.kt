package com.deslomator.tagtimer.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Lbl
import com.deslomator.tagtimer.model.PreSelectedPerson
import com.deslomator.tagtimer.model.PreSelectedPlace
import com.deslomator.tagtimer.model.PreSelectedTag
import com.deslomator.tagtimer.model.Session
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    /*
    EVENT
    */
    @Upsert
    suspend fun upsertEvent(event: Event): Long

    @Delete
    suspend fun deleteEvent(event: Event)

    @Query("DELETE FROM event WHERE sessionId = :sessionId")
    suspend fun deleteEventsForSession(sessionId: Int)

    @Query("SELECT * FROM event WHERE id = :eventId")
    suspend fun getEvent(eventId: Int): Event

    @Query("SELECT * FROM event WHERE sessionId = :sessionId AND inTrash = 0 ORDER BY elapsedTimeMillis ASC")
    fun getActiveEventsForSession(sessionId: Int): Flow<List<Event>>

    @Query("SELECT * FROM event WHERE sessionId = :sessionId AND inTrash = 1 ORDER BY elapsedTimeMillis ASC")
    fun getTrashedEventsForSession(sessionId: Int): Flow<List<Event>>

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
    suspend fun upsertTag(tag: Lbl.Tag)
    @Delete
    suspend fun deleteTag(tag: Lbl.Tag)
    @Query("SELECT * FROM tag WHERE id = :id")
    suspend fun getTag(id: Int): Lbl.Tag
    @Query("SELECT * FROM tag ORDER BY name ASC")
    fun getTags(): Flow<List<Lbl.Tag>>
    @Query("SELECT * FROM tag WHERE inTrash = 0 ORDER BY name ASC")
    fun getActiveTags(): Flow<List<Lbl.Tag>>
    @Query("SELECT * FROM tag WHERE inTrash = 1 ORDER BY name ASC")
    fun getTrashedTags(): Flow<List<Lbl.Tag>>
    
    /*
    PERSON
     */
    @Upsert
    suspend fun upsertPerson(person: Lbl.Person)
    @Delete
    suspend fun deletePerson(person: Lbl.Person)
    @Query("SELECT * FROM person WHERE id = :id")
    suspend fun getPerson(id: Int): Lbl.Person
    @Query("SELECT * FROM person ORDER BY name ASC")
    fun getPersons(): Flow<List<Lbl.Person>>
    @Query("SELECT * FROM person WHERE inTrash = 0 ORDER BY name ASC")
    fun getActivePersons(): Flow<List<Lbl.Person>>
    @Query("SELECT * FROM person WHERE inTrash = 1 ORDER BY name ASC")
    fun getTrashedPersons(): Flow<List<Lbl.Person>>
    
    /*
    PLACE
     */
    @Upsert
    suspend fun upsertPlace(place: Lbl.Place)
    @Delete
    suspend fun deletePlace(place: Lbl.Place)
    @Query("SELECT * FROM place WHERE id = :id")
    suspend fun getPlace(id: Int): Lbl.Place
    @Query("SELECT * FROM place ORDER BY name ASC")
    fun getPlaces(): Flow<List<Lbl.Place>>
    @Query("SELECT * FROM place WHERE inTrash = 0 ORDER BY name ASC")
    fun getActivePlaces(): Flow<List<Lbl.Place>>
    @Query("SELECT * FROM place WHERE inTrash = 1 ORDER BY name ASC")
    fun getTrashedPlaces(): Flow<List<Lbl.Place>>
    
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
    PRESELECTED PERSON
     */
    @Upsert
    suspend fun upsertPreSelectedPerson(preSelectedPerson: PreSelectedPerson)
    @Delete
    suspend fun deletePreSelectedPerson(preSelectedPerson: PreSelectedPerson)
    @Query("DELETE FROM preselectedperson WHERE id = :preSelectedPersonId")
    suspend fun deletePreSelectedPerson(preSelectedPersonId: Int)
    @Query("SELECT * FROM preselectedperson WHERE sessionId = :sessionId")
    fun getPreSelectedPersonsForSession(sessionId: Int): Flow<List<PreSelectedPerson>>
    
    /*
    PRESELECTED PLACE
     */
    @Upsert
    suspend fun upsertPreSelectedPlace(preSelectedPlace: PreSelectedPlace)
    @Delete
    suspend fun deletePreSelectedPlace(preSelectedPlace: PreSelectedPlace)
    @Query("DELETE FROM preselectedplace WHERE id = :preSelectedPlaceId")
    suspend fun deletePreSelectedPlace(preSelectedPlaceId: Int)
    @Query("SELECT * FROM preselectedplace WHERE sessionId = :sessionId")
    fun getPreSelectedPlacesForSession(sessionId: Int): Flow<List<PreSelectedPlace>>
    
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
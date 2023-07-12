package com.deslomator.tagtimer.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Preselected
import com.deslomator.tagtimer.model.Session
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Dao
interface AppDao {
    /*
    EVENTS
    */
    @Upsert
    suspend fun upsertEvent(event: Event): Long

    @Delete
    suspend fun deleteEvent(event: Event)

    @Query("DELETE FROM events WHERE session_id = :sessionId")
    suspend fun deleteEventsForSession(sessionId: Long)

    @Query("SELECT * FROM events WHERE id = :eventId")
    suspend fun getEvent(eventId: Long): Event

    @Query("SELECT * FROM events WHERE session_id = :sessionId AND in_trash = 0 ORDER BY elapsed_time_millis ASC")
    fun getActiveEventsForSession(sessionId: Long): Flow<List<Event>>

    @Query("SELECT * FROM events WHERE session_id = :sessionId AND in_trash = 1 ORDER BY elapsed_time_millis ASC")
    fun getTrashedEventsForSession(sessionId: Long): Flow<List<Event>>

    /*
    SESSIONS
     */
    @Upsert
    suspend fun upsertSession(session: Session): Long

    @Delete
    suspend fun deleteSession(session: Session)

    @Query("SELECT * FROM sessions WHERE id = :id")
    suspend fun getSession(id: Long): Session

    @Query("SELECT * FROM sessions ORDER BY last_access_millis DESC")
    fun getSessions(): Flow<List<Session>>

    @Query("SELECT * FROM sessions ORDER BY last_access_millis DESC")
    suspend fun getSessionsList(): List<Session>

    @Query("SELECT * FROM sessions WHERE in_trash = 0 ORDER BY last_access_millis DESC")
    fun getActiveSessions(): Flow<List<Session>>

    @Query("SELECT * FROM sessions WHERE in_trash = 1 ORDER BY last_access_millis DESC")
    fun getTrashedSessions(): Flow<List<Session>>
    
    /*
    TAGS
     */
    @Upsert
    suspend fun upsertTag(tag: Label.Tag)
    @Delete
    suspend fun deleteTag(tag: Label.Tag)
    @Query("SELECT * FROM tags WHERE in_trash = 0 ORDER BY name ASC")
    fun getActiveTags(): Flow<List<Label.Tag>>
    @Query("SELECT * FROM tags WHERE in_trash = 1 ORDER BY name ASC")
    fun getTrashedTags(): Flow<List<Label.Tag>>
    
    /*
    PERSONS
     */
    @Upsert
    suspend fun upsertPerson(person: Label.Person)
    @Delete
    suspend fun deletePerson(person: Label.Person)
    @Query("SELECT * FROM persons WHERE in_trash = 0 ORDER BY name ASC")
    fun getActivePersons(): Flow<List<Label.Person>>
    @Query("SELECT * FROM persons WHERE in_trash = 1 ORDER BY name ASC")
    fun getTrashedPersons(): Flow<List<Label.Person>>
    
    /*
    PLACES
     */
    @Upsert
    suspend fun upsertPlace(place: Label.Place)
    @Delete
    suspend fun deletePlace(place: Label.Place)
    @Query("SELECT * FROM places WHERE in_trash = 0 ORDER BY name ASC")
    fun getActivePlaces(): Flow<List<Label.Place>>
    @Query("SELECT * FROM places WHERE in_trash = 1 ORDER BY name ASC")
    fun getTrashedPlaces(): Flow<List<Label.Place>>
    
    /*
    PRESELECTED TAGS
     */
    @Upsert
    suspend fun upsertPreSelectedTag(preSelectedTag: Preselected.Tag)
    @Delete
    suspend fun deletePreSelectedTag(preSelectedTag: Preselected.Tag)
    @Query("SELECT * FROM ps_tags WHERE session_id = :sessionId")
    fun getPreSelectedTagsForSession(sessionId: Long): Flow<List<Preselected.Tag>>
    @Query("SELECT * FROM ps_tags WHERE session_id = :sessionId")
    suspend fun getPreSelectedTagsListForSession(sessionId: Long): List<Preselected.Tag>
    
    /*
    PRESELECTED PERSONS
     */
    @Upsert
    suspend fun upsertPreSelectedPerson(preSelectedPerson: Preselected.Person)
    @Delete
    suspend fun deletePreSelectedPerson(preSelectedPerson: Preselected.Person)
    @Query("SELECT * FROM ps_persons WHERE session_id = :sessionId")
    fun getPreSelectedPersonsForSession(sessionId: Long): Flow<List<Preselected.Person>>
    @Query("SELECT * FROM ps_persons WHERE session_id = :sessionId")
    suspend fun getPreSelectedPersonsListForSession(sessionId: Long): List<Preselected.Person>
    
    /*
    PRESELECTED PLACES
     */
    @Upsert
    suspend fun upsertPreSelectedPlace(preSelectedPlace: Preselected.Place)
    @Delete
    suspend fun deletePreSelectedPlace(preSelectedPlace: Preselected.Place)
    @Query("SELECT * FROM ps_places WHERE session_id = :sessionId")
    fun getPreSelectedPlacesForSession(sessionId: Long): Flow<List<Preselected.Place>>
    @Query("SELECT * FROM ps_places WHERE session_id = :sessionId")
    suspend fun getPreSelectedPlacesListForSession(sessionId: Long): List<Preselected.Place>
    
    /*
    ORPHANS
     */
    @Query("DELETE FROM events " +
            "WHERE NOT EXISTS (SELECT NULL FROM sessions WHERE events.session_id = sessions.id)")
    suspend fun clearOrphanEvents(): Int
    @Query("DELETE FROM ps_persons " +
            "WHERE NOT EXISTS (SELECT NULL FROM sessions WHERE ps_persons.session_id = sessions.id)")
    suspend fun clearOrphanPreSelectedPersons(): Int
    @Query("DELETE FROM ps_places " +
            "WHERE NOT EXISTS (SELECT NULL FROM sessions WHERE ps_places.session_id = sessions.id)")
    suspend fun clearOrphanPreSelectedPlaces(): Int
    @Query("DELETE FROM ps_tags " +
            "WHERE NOT EXISTS (SELECT NULL FROM sessions WHERE ps_tags.session_id = sessions.id)")
    suspend fun clearOrphanPreSelectedTags(): Int

    /*
    BACKUP AND RESTORE
     */
    @Query("SELECT * FROM persons WHERE in_trash = 0")
    suspend fun getActivePersonsList(): List<Label.Person>
    @Query("SELECT * FROM places WHERE in_trash = 0")
    suspend fun getActivePlacesList(): List<Label.Place>
    @Query("SELECT * FROM tags WHERE in_trash = 0")
    suspend fun getActiveTagsList(): List<Label.Tag>

    @Query("SELECT * FROM persons")
    suspend fun getAllPersonsList(): List<Label.Person>
    @Query("SELECT * FROM places")
    suspend fun getAllPlacesList(): List<Label.Place>
    @Query("SELECT * FROM tags")
    suspend fun getAllTagsList(): List<Label.Tag>
    @Query("SELECT * FROM ps_persons")
    suspend fun getAllPreselectedPersonsList(): List<Preselected.Person>
    @Query("SELECT * FROM ps_places")
    suspend fun getAllPreselectedPlacesList(): List<Preselected.Place>
    @Query("SELECT * FROM ps_tags")
    suspend fun getAllPreselectedTagsList(): List<Preselected.Tag>
    @Query("SELECT * FROM sessions")
    suspend fun getAllSessionsList(): List<Session>
    @Query("SELECT * FROM events")
    suspend fun getAllEventsList(): List<Event>
    /*
    DELETE
     */
    @Query("DELETE FROM events")
    suspend fun deleteAllEvents()
    @Query("DELETE FROM persons")
    suspend fun deleteAllPersons()
    @Query("DELETE FROM places")
    suspend fun deleteAllPlaces()
    @Query("DELETE FROM tags")
    suspend fun deleteAllTags()
    @Query("DELETE FROM ps_persons")
    suspend fun deleteAllPreselectedPersons()
    @Query("DELETE FROM ps_places")
    suspend fun deleteAllPreselectedPlaces()
    @Query("DELETE FROM ps_tags")
    suspend fun deleteAllPreselectedTags()
    @Query("DELETE FROM sessions")
    suspend fun deleteAllSessions()
    suspend fun deleteAllData() {
        withContext(Dispatchers.IO) {
            launch { deleteAllEvents() }
            launch { deleteAllPersons() }
            launch { deleteAllPlaces() }
            launch { deleteAllTags() }
            launch { deleteAllPreselectedPersons() }
            launch { deleteAllPreselectedPlaces() }
            launch { deleteAllPreselectedTags() }
            launch { deleteAllSessions() }
        }
    }
}

private const val TAG ="AppDao"
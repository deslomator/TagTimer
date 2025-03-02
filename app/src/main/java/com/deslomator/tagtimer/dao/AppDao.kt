package com.deslomator.tagtimer.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.EventForDisplay
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Preference
import com.deslomator.tagtimer.model.Preselected
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.type.LabelSort
import com.deslomator.tagtimer.model.type.LabelType
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
    @Upsert
    suspend fun upsertEvents(events: List<Event>): List<Long>

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

    @Query("SELECT * FROM events WHERE session_id = :sessionId AND in_trash = 0 ORDER BY elapsed_time_millis ASC")
    fun getEventsForDisplay(sessionId: Long): Flow<List<EventForDisplay>>

    @Query("SELECT * FROM events WHERE session_id = :sessionId AND in_trash = 1 ORDER BY elapsed_time_millis ASC")
    fun getTrashedEventsForDisplay(sessionId: Long): Flow<List<EventForDisplay>>

    /*
    SESSIONS
     */
    @Upsert
    suspend fun upsertSession(session: Session): Long
    @Upsert
    suspend fun upsertSessions(sessions: List<Session>): List<Long>

    @Delete
    suspend fun deleteSession(session: Session)

    @Query("SELECT * FROM sessions WHERE id = :id")
    suspend fun getSession(id: Long): Session

    @Query("SELECT * FROM sessions ORDER BY last_access_millis DESC")
    fun getSessions(): Flow<List<Session>>

    @Query("SELECT * FROM sessions ORDER BY last_access_millis DESC")
    suspend fun getSessionsList(): List<Session>

    @Query("SELECT * FROM sessions WHERE in_trash = 0 ORDER BY last_access_millis DESC")
    fun getSessionsByLastAccess(): Flow<List<Session>>

    @Query("SELECT * FROM sessions WHERE in_trash = 0 ORDER BY session_date_millis DESC")
    fun getSessionsByDate(): Flow<List<Session>>

    @Query("SELECT * FROM sessions WHERE in_trash = 0 ORDER BY name ASC")
    fun getSessionsByName(): Flow<List<Session>>

    @Query("SELECT * FROM sessions WHERE in_trash = 1 ORDER BY last_access_millis DESC")
    fun getTrashedSessions(): Flow<List<Session>>

    /*
    LABELS
     */
    @Upsert
    suspend fun upsertLabel(label: Label)

    @Upsert
    suspend fun upsertLabels(label: List<Label>)

    @Delete
    suspend fun deleteLabel(label: Label)

    @Query("SELECT * FROM labels WHERE type = :type AND in_trash = 0")
    fun getActiveLabels(type: Int): Flow<List<Label>>

    @Query("SELECT * FROM labels WHERE type = :type AND in_trash = 1 ORDER BY name ASC")
    fun getTrashedLabels(type: Int): Flow<List<Label>>

    /*
    TAGS
     */
    //fun getActiveTags() = getActiveLabels(LabelType.TAG.typeId)
    fun getTrashedTags() = getTrashedLabels(LabelType.TAG.typeId)
    /*
    PERSONS
     */
    //fun getActivePersons() = getActiveLabels(LabelType.PERSON.typeId)
    fun getTrashedPersons() = getTrashedLabels(LabelType.PERSON.typeId)

    /*
    PLACES
     */
    //fun getActivePLaces() = getActiveLabels(LabelType.PLACE.typeId)
    fun getTrashedPlaces() = getTrashedLabels(LabelType.PLACE.typeId)


    /*
    PRESELECTED
     */
    @Upsert
    suspend fun upsertPreSelectedLabel(preSelectedLabel: Preselected)

    @Upsert
    suspend fun upsertPreSelectedLabels(preSelectedLabels: List<Preselected>)

    @Delete
    suspend fun deletePreSelectedLabel(preSelectedLabel: Preselected)

    @Query("SELECT name, color, in_trash, type, id FROM preselected JOIN labels ON label_id = labels.id WHERE session_id = :sessionId AND type = :type")
    fun getPreSelectedLabelsForSession(sessionId: Long, type: Int): Flow<List<Label>>

    @Query("SELECT name, color, in_trash, type, id FROM preselected JOIN labels ON label_id = labels.id WHERE session_id = :sessionId AND type = :type")
    suspend fun getPreSelectedLabelsListForSession(sessionId: Long, type: Int): List<Label>

    /*
    PRESELECTED TAGS
     */
    //fun getPreSelectedTagsForSession(sessionId: Long) = getPreSelectedLabelsForSession(sessionId, LabelType.TAG.typeId)
    suspend fun getPreSelectedTagsListForSession(sessionId: Long) = getPreSelectedLabelsListForSession(sessionId, LabelType.TAG.typeId)

    /*
    PRESELECTED PERSONS
     */
    //fun getPreSelectedPersonsForSession(sessionId: Long) = getPreSelectedLabelsForSession(sessionId, LabelType.PERSON.typeId)
    suspend fun getPreSelectedPersonsListForSession(sessionId: Long) = getPreSelectedLabelsListForSession(sessionId, LabelType.PERSON.typeId)

    /*
    PRESELECTED PLACES
     */
    //fun getPreSelectedPlacesForSession(sessionId: Long) = getPreSelectedLabelsForSession(sessionId, LabelType.PLACE.typeId)
    suspend fun getPreSelectedPlacesListForSession(sessionId: Long) = getPreSelectedLabelsListForSession(sessionId, LabelType.PLACE.typeId)

    /*
    ORPHANS
     */
    @Query("DELETE FROM events WHERE NOT EXISTS (SELECT NULL FROM sessions WHERE events.session_id = sessions.id)")
    suspend fun clearOrphanEvents(): Int

    @Query("DELETE FROM preselected WHERE NOT EXISTS (SELECT NULL FROM sessions WHERE preselected.session_id = sessions.id)")
    suspend fun clearOrphanPreSelected(): Int

    /*
    BACKUP AND RESTORE
     */
    @Query("SELECT * FROM labels WHERE in_trash = 0")
    suspend fun getActiveLabelsList(): List<Label>

    @Query("SELECT * FROM labels")
    suspend fun getAllLabelsList(): List<Label>

    @Query("SELECT * FROM preselected")
    suspend fun getAllPreselectedLabelsList(): List<Preselected>

    @Query("SELECT * FROM sessions")
    suspend fun getAllSessionsList(): List<Session>

    @Query("SELECT * FROM events")
    suspend fun getAllEventsList(): List<Event>

    /*
    DELETE
     */
    @Query("DELETE FROM events")
    suspend fun deleteAllEvents()

    @Query("DELETE FROM labels")
    suspend fun deleteAllLabels()


    @Query("DELETE FROM preselected")
    suspend fun deleteAllPreselectedLabels()

    @Query("DELETE FROM sessions")
    suspend fun deleteAllSessions()

    @Query("DELETE FROM preferences")
    suspend fun deleteAllPreferences()

    suspend fun deleteAllData() {
        withContext(Dispatchers.IO) {
            launch { deleteAllEvents() }
            launch { deleteAllLabels() }
            launch { deleteAllPreselectedLabels() }
            launch { deleteAllSessions() }
            launch { deleteAllPreferences() }
        }
    }

    /*
    PREFERENCES
     */
    @Upsert
    suspend fun upsertPreference(preference: Preference)

    @Upsert
    suspend fun upsertPreferences(preference: List<Preference>)

    @Delete
    suspend fun deletePreference(preference: Preference)

    @Query("SELECT * FROM preferences")
    fun getPreferences(): Flow<List<Preference>>

    @Query("SELECT value FROM preferences WHERE prefKey = :prefKey")
    fun getPreferenceValue(prefKey: String): Flow<String>

    @Query("SELECT * FROM preferences")
    fun getAllPreferencesList(): List<Preference>

    @Query("SELECT * FROM preferences WHERE prefKey = :key")
    suspend fun getPreference(key: String): Preference
}

private const val TAG ="AppDao"
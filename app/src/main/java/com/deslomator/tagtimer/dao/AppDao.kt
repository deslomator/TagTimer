package com.deslomator.tagtimer.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.deslomator.tagtimer.model.DbBackup
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.ancillary.EventForDisplay
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Preference
import com.deslomator.tagtimer.model.Selected
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.type.ItemState
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

    @Query("DELETE FROM events WHERE id = :eventId")
    suspend fun deleteEvent(eventId: Long)

    @Query("DELETE FROM events WHERE session_id = :sessionId")
    suspend fun deleteEventsForSession(sessionId: Long)

    @Query("SELECT * FROM events WHERE id = :eventId")
    suspend fun getEvent(eventId: Long): Event

    @Query("SELECT * FROM events WHERE session_id = :sessionId AND in_trash = 0 ORDER BY elapsed_time_millis ASC")
    fun getActiveEventsForSession(sessionId: Long): Flow<List<Event>>

    @Query("SELECT * FROM events WHERE session_id = :sessionId AND in_trash = 1 ORDER BY elapsed_time_millis ASC")
    fun getTrashedEventsForSession(sessionId: Long): Flow<List<Event>>

    /**
     * when an Event is swiped to trash, the EventForDisplay
     * provided in the action is stale, so we retrieve the
     * updated one from the database
    **/
    suspend fun trashEvent(id: Long) { upsertEvent(getEvent(id).copy(inTrash = true)) }

    /*
    EVENTS FOR DISPLAY
     */
    @Transaction
    @Query("SELECT * FROM events WHERE session_id = :sessionId AND in_trash = 0 ORDER BY elapsed_time_millis ASC")
    fun getEventsForDisplay(sessionId: Long): Flow<List<EventForDisplay>>

    @Transaction
    @Query("SELECT * FROM events WHERE session_id = :sessionId AND in_trash = 1 ORDER BY elapsed_time_millis ASC")
    fun getTrashedEventsForDisplay(sessionId: Long): Flow<List<EventForDisplay>>

    @Transaction
    @Query("SELECT * FROM events WHERE session_id = :sessionId AND in_trash = 0 ORDER BY elapsed_time_millis ASC")
    suspend fun getEventsForDisplayList(sessionId: Long): List<EventForDisplay>

    /*
    SESSIONS
     */
    @Upsert
    suspend fun upsertSession(session: Session): Long
    @Upsert
    suspend fun upsertSessions(sessions: List<Session>): List<Long>

    @Delete
    suspend fun deleteSession(session: Session)

    @Query("DELETE FROM sessions WHERE id = :sessionId")
    suspend fun deleteSession(sessionId: Long)

    @Query("SELECT * FROM sessions WHERE id = :id")
    suspend fun getSession(id: Long): Session

    @Query("SELECT * FROM sessions ORDER BY last_access_millis DESC")
    fun getSessions(): Flow<List<Session>>

    @Query("SELECT * FROM sessions WHERE state = :state ORDER BY last_access_millis DESC")
    fun getSessionsByLastAccess(state: String = ItemState.ENABLED.name): Flow<List<Session>>

    @Query("SELECT * FROM sessions WHERE state = :state ORDER BY session_date_millis DESC")
    fun getSessionsByDate(state: String = ItemState.ENABLED.name): Flow<List<Session>>

    @Query("SELECT * FROM sessions WHERE state = :state ORDER BY name ASC")
    fun getSessionsByName(state: String = ItemState.ENABLED.name): Flow<List<Session>>

    @Query("SELECT * FROM sessions WHERE state = :state ORDER BY last_access_millis DESC")
    fun getTrashedSessions(state: String = ItemState.TRASHED.name): Flow<List<Session>>

    /*
    LABELS
     */
    @Upsert
    suspend fun upsertLabel(label: Label)

    @Upsert
    suspend fun upsertLabels(labels: List<Label>)

    @Delete
    suspend fun deleteLabel(label: Label)

    @Query("DELETE FROM labels WHERE id = :labelId")
    suspend fun deleteLabel(labelId: Long)

    @Query("SELECT * FROM labels WHERE type = :type")
    fun getLabels(type: LabelType): Flow<List<Label>>

    @Query("SELECT COUNT(*) FROM events WHERE tag_id = :labelId OR person_id = :labelId OR place_id = :labelId")
    suspend fun getEventCountForLabel(labelId: Long): Int

    @Query("SELECT * FROM labels WHERE state = :state AND type = :type")
    suspend fun getAllActiveLabelsList(state: ItemState, type: LabelType): List<Label>

    suspend fun getAllActiveTagsList() = getAllActiveLabelsList(ItemState.ENABLED, LabelType.TAG)
    suspend fun getAllActivePersonsList() = getAllActiveLabelsList(ItemState.ENABLED, LabelType.PERSON)
    suspend fun getAllActivePlacesList() = getAllActiveLabelsList(ItemState.ENABLED, LabelType.PLACE)

    /*
    TAGS
     */
    fun getTags() = getLabels(LabelType.TAG)
    /*
    PERSONS
     */
    fun getPersons() = getLabels(LabelType.PERSON)

    /*
    PLACES
     */
    fun getPLaces() = getLabels(LabelType.PLACE)

    /*
    USED LABELS
     */
    @Query("SELECT * FROM labels WHERE id IN (SELECT DISTINCT tag_id FROM events WHERE session_id = :sessionId)")
    fun getUsedTags(sessionId: Long): Flow<List<Label>>

    @Query("SELECT * FROM labels WHERE id IN (SELECT DISTINCT person_id FROM events WHERE session_id = :sessionId)")
    fun getUsedPersons(sessionId: Long): Flow<List<Label>>

    @Query("SELECT * FROM labels WHERE id IN (SELECT DISTINCT place_id FROM events WHERE session_id = :sessionId)")
    fun getUsedPlaces(sessionId: Long): Flow<List<Label>>

    /*
    SELECTED
     */
    @Upsert
    suspend fun upsertSelected(selectedLabel: Selected)

    @Upsert
    suspend fun upsertSelected(selectedLabels: List<Selected>)

    @Delete
    suspend fun deleteSelected(selectedLabel: Selected)

    @Query("SELECT name, color, state, type, id FROM selected JOIN labels ON label_id = labels.id WHERE session_id = :sessionId AND type = :type")
    fun getSelectedLabelsForSession(sessionId: Long, type: LabelType): Flow<List<Label>>

    @Query("SELECT name, color, state, type, id FROM selected JOIN labels ON label_id = labels.id WHERE session_id = :sessionId AND type = :type")
    suspend fun getSelectedLabelsListForSession(sessionId: Long, type: LabelType): List<Label>

    @Query("SELECT session_id, label_id FROM selected WHERE session_id = :sessionId")
    suspend fun getSelectedLabelsListForSession(sessionId: Long): List<Selected>

    @Query("DELETE FROM selected WHERE session_id = :sessionId")
    suspend fun  deleteSelectedLabelsForSession(sessionId: Long)

    /*
    SELECTED TAGS
     */
    fun getSelectedTagsForSession(sessionId: Long) = getSelectedLabelsForSession(sessionId, LabelType.TAG)

    suspend fun getSelectedTagsListForSession(sessionId: Long) =
        getSelectedLabelsListForSession(sessionId, LabelType.TAG)

    /*
    SELECTED PERSONS
     */
    fun getSelectedPersonsForSession(sessionId: Long) = getSelectedLabelsForSession(sessionId, LabelType.PERSON)

    suspend fun getSelectedPersonsListForSession(sessionId: Long) =
        getSelectedLabelsListForSession(sessionId, LabelType.PERSON)

    /*
    SELECTED PLACES
     */
    fun getSelectedPlacesForSession(sessionId: Long) = getSelectedLabelsForSession(sessionId, LabelType.PLACE)

    suspend fun getSelectedPlacesListForSession(sessionId: Long) =
        getSelectedLabelsListForSession(sessionId, LabelType.PLACE)

    /*
    ORPHANS
     */
    @Query("DELETE FROM events WHERE NOT EXISTS (SELECT NULL FROM sessions WHERE events.session_id = sessions.id)")
    suspend fun clearOrphanEvents(): Int

    @Query("DELETE FROM selected WHERE NOT EXISTS (SELECT NULL FROM sessions WHERE selected.session_id = sessions.id)")
    suspend fun clearOrphanSelected(): Int

    /*
    BACKUP AND RESTORE
     */

    @Query("SELECT * FROM labels")
    suspend fun getAllLabelsList(): List<Label>

    @Query("SELECT * FROM selected")
    suspend fun getAllSelectedLabelsList(): List<Selected>

    @Query("SELECT * FROM sessions")
    suspend fun getAllSessionsList(): List<Session>

    @Query("SELECT * FROM events")
    suspend fun getAllEventsList(): List<Event>


    /**
     * Sessions and Labels must be inserted before events
     * and selected to make sure there aren't any
     * foreign key constraint violations
     */
    @Transaction
    suspend fun fullRestore(dbBackup: DbBackup) {
        withContext(Dispatchers.IO) {
            launch {
                val job = launch {
                    Log.i(TAG, "deleting sessions and labels")
                    deleteAllSessions()
                    deleteAllLabels()
                }
                job.join()
                Log.i(TAG, "inserting sessions")
                upsertSessions(dbBackup.sessions)
                Log.i(TAG, "inserting labels")
                upsertLabels(dbBackup.labels)
                Log.i(TAG, "inserting events")
                upsertEvents(dbBackup.events)
                Log.i(TAG, "inserting selected")
                upsertSelected(dbBackup.selected)
            }
            launch {
                val job = launch {
                    Log.i(TAG, "deleting preferences")
                    deleteAllPreferences()
                }
                job.join()
                Log.i(TAG, "inserting preferences")
                upsertPreferences(dbBackup.prefs)
            }
        }
    }

    /*
    DELETE
     */
    @Query("DELETE FROM events")
    suspend fun deleteAllEvents()

    @Query("DELETE FROM labels")
    suspend fun deleteAllLabels()


    @Query("DELETE FROM selected")
    suspend fun deleteAllSelectedLabels()

    @Query("DELETE FROM sessions")
    suspend fun deleteAllSessions()

    @Query("DELETE FROM preferences")
    suspend fun deleteAllPreferences()

    /*
    PREFERENCES
     */
    @Upsert
    suspend fun upsertPreference(preference: Preference)

    @Upsert
    suspend fun upsertPreferences(preference: List<Preference>)

    @Query("SELECT * FROM preferences")
    fun getPreferences(): Flow<List<Preference>>

    @Query("SELECT value FROM preferences WHERE prefKey = :prefKey")
    fun getPreferenceValue(prefKey: String): Flow<String>

    @Query("SELECT * FROM preferences")
    suspend fun getAllPreferencesList(): List<Preference>

}

private const val TAG ="AppDao"
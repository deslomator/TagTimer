package com.deslomator.tagtimer.util

import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.DbBackup
import com.deslomator.tagtimer.model.type.Result
import com.deslomator.tagtimer.viewmodel.isEmpty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.json.JSONException
import java.io.FileInputStream
import java.lang.IllegalArgumentException

/**
 * [json] is a string previously decoded from an Uri
 * processed by contentResolver (from the user tapping
 * on a file in a file manager, for example),
 * It also can receive the string from its own overload below
 */
fun restoreBackup(appDao: AppDao, json: String): Result {
    var result: Result = Result.RestoreFailed
    runBlocking(Dispatchers.IO) {
        try {
            val dbBackup = Json.decodeFromString<DbBackup>(json)
            result = if (dbBackup.isEmpty()) {
                Log.e("$TAG FromString()", "Failed, backup class is empty")
                Result.NothingToBackup
            } else {
                appDao.deleteAllData()
                runBlocking {
                    launch { dbBackup.persons.forEach { appDao.upsertPerson(it) } }
                    launch { dbBackup.places.forEach { appDao.upsertPlace(it) } }
                    launch { dbBackup.tags.forEach { appDao.upsertTag(it) } }
                    launch { dbBackup.preselectedPersons.forEach { appDao.upsertPreSelectedPerson(it) } }
                    launch { dbBackup.preselectedPlaces.forEach { appDao.upsertPreSelectedPlace(it) } }
                    launch { dbBackup.preselectedTags.forEach { appDao.upsertPreSelectedTag(it) } }
                    launch { dbBackup.events.forEach { appDao.upsertEvent(it) } }
                    launch { dbBackup.sessions.forEach { appDao.upsertSession(it) } }
                }
                Log.i(TAG, "FromString() Restore success")
                Result.Restored
            }
        } catch (e: SerializationException) {
            result = Result.BadFile
            Log.e(TAG, "FromString() SerializationException: $e")
        } catch (e: IllegalArgumentException) {
            result = Result.BadFile
            Log.e(TAG, "FromString() IllegalArgumentException: $e")
        } catch (e: Exception) {
            Log.e(TAG, "FromString() Exception: $e")
        }
    }
    return result
}

/**
 * [uri] for now is a reference to a file in the
 * app's FilesDir, when the uri comes from an Intent
 * the file is retrieved by contentProvider
 * and converted into a string
 */
fun restoreBackup(appDao: AppDao, uri: Uri): Result {
    var result: Result = Result.RestoreFailed
    runBlocking(Dispatchers.IO) {
        try {
            val json = FileInputStream(uri.toFile()).use { fis ->
                fis.readBytes()
            }.decodeToString()
            result = restoreBackup(appDao, json)
        } catch (e: Exception) {
            Log.e("$TAG FromUri()", e.message.toString())
        }
    }
    return result
}

private const val TAG = "RestoreBackup"
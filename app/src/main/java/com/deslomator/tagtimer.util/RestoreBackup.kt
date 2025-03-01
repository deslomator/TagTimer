package com.deslomator.tagtimer.util

import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.DbBackup
import com.deslomator.tagtimer.model.type.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.FileInputStream

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
            if (dbBackup.isEmpty()) {
                Log.e(TAG, "FromString(). Failed, backup class is empty")
                result = Result.NothingToBackup
            } else { // do not erase anything if it's a labels only backup
                Log.i(TAG, "FromString(). Inserting labels, nothing is deleted")
                runBlocking {
                    launch { appDao.upsertLabels(dbBackup.labels) }
                    Log.i(TAG, "FromString() Restore of labels success")
                    if (!dbBackup.isLabelsOnly()) {
                        runBlocking {
                            Log.i(TAG, "FromString() Deleting current data")
                            appDao.deleteAllData()
                            launch { appDao.upsertPreSelectedLabels(dbBackup.preselected) }
                            launch {  appDao.upsertEvents(dbBackup.events) }
                            launch { appDao.upsertSessions(dbBackup.sessions) }
                            launch { appDao.upsertPreferences(dbBackup.prefs) }
                        }
                        Log.i(TAG, "FromString() Restore of full backup success")
                    }
                }
                result = Result.Restored
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
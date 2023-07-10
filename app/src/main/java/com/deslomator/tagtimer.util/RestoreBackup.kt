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
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream
import java.io.FileInputStream

/**
 * [json] is a string previously decoded from an Uri
 * processed by contentResolver (from the user tapping
 * on a file in a file manager, for example),
 * It also can receive the string from its own overload below
 */
fun restoreBackup(appDao: AppDao, json: String): Result {
    var result = Result.RESTORE_FAILED
    runBlocking {
        withContext(Dispatchers.IO) {
            try {
                val dbBackup = Json.decodeFromString<DbBackup>(json)
                result = if (dbBackup.isEmpty()) {
                    Log.e("$TAG FromString()", "Failed, backup class is empty")
                    Result.NOTHING_TO_BACKUP
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
                    Log.i("$TAG FromString()", "Restore success")
                    Result.RESTORED
                }
            } catch (error: Error) {
                Log.e("$TAG FromString()", error.message.toString())
            }
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
    var result = Result.RESTORE_FAILED
    runBlocking {
        withContext(Dispatchers.IO) {
            try {
                val json = FileInputStream(uri.toFile()).use { fis ->
                    fis.readBytes()
                }.decodeToString()
                result = restoreBackup(appDao, json)
            } catch (error: Error) {
                Log.e("$TAG FromUri()", error.message.toString())
            }
        }
    }
    return result
}

private const val TAG = "RestoreBackup"
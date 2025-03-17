package com.deslomator.tagtimer.util

import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.DbBackup
import com.deslomator.tagtimer.model.type.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.FileInputStream

/**
 * @param uri is a reference to a file in the
 * app's FilesDir; when the uri comes from an Intent
 * the file is retrieved by contentResolver
 * and copied into a tempFile in cacheDir
 */
suspend fun restoreBackup(appDao: AppDao, uri: Uri): Result {
    var result: Result = Result.RestoreFailed
    try {
        var bytes: String?
        withContext(Dispatchers.IO) {
            bytes = FileInputStream(uri.toFile()).use { fis ->
                fis.readBytes()
            }.decodeToString()
        }
        val dbBackup = Json.decodeFromString<DbBackup>(bytes!!)
        if (dbBackup.isEmpty()) {
            throw EmptyDatabaseException()
        } else { // do not erase anything if it's a labels only backup
            if (dbBackup.isLabelsOnly()) {
                Log.i(TAG, "restoreBackup(). Inserting labels, nothing is deleted")
                withContext(Dispatchers.IO) {
                    launch { appDao.upsertLabels(dbBackup.labels) }
                }
                Log.i(TAG, "FromString() Restore of labels success")
                result = Result.Restored
            } else {
                result = Result.WarnFullDeletion(dbBackup)
            }
        }
    } catch (e: EmptyDatabaseException) {
        Log.e(TAG, "FromString() $e")
        result = Result.NothingToRestore
    } catch (e: SerializationException) {
        result = Result.BadFile
        Log.e(TAG, "FromString() SerializationException: $e")
    } catch (e: IllegalArgumentException) {
        result = Result.BadFile
        Log.e(TAG, "FromString() IllegalArgumentException: $e")
    } catch (e: Exception) {
        Log.e(TAG, "FromString() Exception: $e")
    }
    return result
}

private const val TAG = "RestoreBackup"
package com.deslomator.tagtimer.util

import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.FullBackup
import com.deslomator.tagtimer.model.type.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.FileReader

suspend fun restoreFullBackup(appDao: AppDao, uri: Uri): Result {
    var result = Result.RESTORED
    withContext(Dispatchers.IO) {
        val file = uri.toFile()
        val reader = BufferedReader(FileReader(file))
        val json = reader.readText()
        reader.close()
        try {
            val full = Json.decodeFromString<FullBackup>(json)
            full.persons.forEach { appDao.upsertPerson(it) }
            full.places.forEach { appDao.upsertPlace(it) }
            full.tags.forEach { appDao.upsertTag(it) }
            full.preselectedPersons.forEach { appDao.upsertPreSelectedPerson(it) }
            full.preselectedPlaces.forEach { appDao.upsertPreSelectedPlace(it) }
            full.preselectedTags.forEach { appDao.upsertPreSelectedTag(it) }
            full.events.forEach { appDao.upsertEvent(it) }
            full.sessions.forEach { appDao.upsertSession(it) }
        } catch (error: Error) {
            Log.d("restoreFullBackup()", error.message.toString())
            result = Result.RESTORE_FAILED
        }
    }
    return result
}
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
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream
import java.io.FileInputStream

suspend fun restoreBackup(appDao: AppDao, uri: Uri): Result {
    var result = Result.RESTORED
    withContext(Dispatchers.IO) {
        var fis: FileInputStream? = null
        val fos: ByteArrayOutputStream?
        try {
            fis = FileInputStream(uri.toFile())
            fos = ByteArrayOutputStream(fis.available())
            val bis = fis.buffered()
            val bos = fos.buffered()
            val buf = ByteArray(fis.available())
            bis.read(buf)
            do {
                bos.write(buf)
            } while (bis.read(buf) != -1)
            val json = fos.toString()
            Log.d("restoreBackup()", "file has been read: $json")
            val dbBackup = Json.decodeFromString<DbBackup>(json)
            if (dbBackup.isEmpty()) {
                result = Result.NOTHING_TO_BACKUP
            } else {
                val job = launch {
                    launch { dbBackup.persons.forEach { appDao.upsertPerson(it) } }
                    launch { dbBackup.places.forEach { appDao.upsertPlace(it) } }
                    launch { dbBackup.tags.forEach { appDao.upsertTag(it) } }
                    launch { dbBackup.preselectedPersons.forEach { appDao.upsertPreSelectedPerson(it) } }
                    launch { dbBackup.preselectedPlaces.forEach { appDao.upsertPreSelectedPlace(it) } }
                    launch { dbBackup.preselectedTags.forEach { appDao.upsertPreSelectedTag(it) } }
                    launch { dbBackup.events.forEach { appDao.upsertEvent(it) } }
                    launch { dbBackup.sessions.forEach { appDao.upsertSession(it) } }
                }
                job.join()
            }
        } catch (error: Error) {
            Log.e("restoreBackup()", error.message.toString())
            result = Result.RESTORE_FAILED
        } finally {
            fis?.close()
        }
        Log.d("restoreBackup()", "finishing, result: $result")
        return@withContext
    }
    return result
}
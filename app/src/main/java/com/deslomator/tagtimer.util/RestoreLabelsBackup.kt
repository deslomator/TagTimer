package com.deslomator.tagtimer.util

import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.BackedLabels
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.state.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.FileReader

suspend fun restoreLabelsBackup(appDao: AppDao, uri: Uri): Result {
    var result = Result.RESTORED

    withContext(Dispatchers.IO) {
        val file = uri.toFile()
        val reader = BufferedReader(FileReader(file))
        val json = reader.readText()
        reader.close()
        try {
            val labels = Json.decodeFromString<BackedLabels>(json)
            Log.d("restoreLabelsBackup", "labels: $labels")
            // check if there's something to restore
            var wasSomethingRestored = false
            if (labels.persons.isNotEmpty()) {
                val currentPersons = appDao.getActivePersonsList()
                    .map { Pair(it.name, it.color) }
                val personsToRestore = labels.persons
                    .filter { !currentPersons.contains(Pair(it.name, it.color)) }
                wasSomethingRestored = wasSomethingRestored or personsToRestore.isNotEmpty()
                personsToRestore.forEach {
                    appDao.upsertPerson(
                        Label.Person(
                            name = it.name,
                            color = it.color
                        )
                    )
                }
            }
            if (labels.places.isNotEmpty()) {
                val currentPlaces = appDao.getActivePlacesList()
                    .map { Pair(it.name, it.color) }
                val placesToRestore = labels.places
                    .filter { !currentPlaces.contains(Pair(it.name, it.color)) }
                wasSomethingRestored = wasSomethingRestored or placesToRestore.isNotEmpty()
                placesToRestore.forEach {
                    appDao.upsertPlace(
                        Label.Place(
                            name = it.name,
                            color = it.color
                        )
                    )
                }
            }
            if (labels.tags.isNotEmpty()) {
                val currentTags = appDao.getActiveTagsList()
                    .map { Pair(it.name, it.color) }
                val tagsToRestore = labels.tags
                    .filter { !currentTags.contains(Pair(it.name, it.color)) }
                wasSomethingRestored = wasSomethingRestored or tagsToRestore.isNotEmpty()
                tagsToRestore.forEach {
                    appDao.upsertTag(
                        Label.Tag(
                            name = it.name,
                            color = it.color
                        )
                    )
                }
            }
            if (!wasSomethingRestored) result = Result.NOTHING_TO_RESTORE
        } catch (error: Error) {
            Log.d("restoreLabelsBackup()", error.message.toString())
            result = Result.RESTORE_FAILED
        }
    }
    return result
}
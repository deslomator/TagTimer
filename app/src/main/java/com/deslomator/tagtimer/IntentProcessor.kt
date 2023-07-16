package com.deslomator.tagtimer

import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.util.Consumer
import androidx.documentfile.provider.DocumentFile
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.DbBackup
import com.deslomator.tagtimer.model.type.Result
import com.deslomator.tagtimer.util.EmptyDatabaseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.FileNotFoundException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntentProcessor(
    intent: Intent,
    activity: ComponentActivity,
    appDao: AppDao
) {
    var dbBackup: DbBackup? by rememberSaveable { mutableStateOf(null) }
    var intentState: Intent? by rememberSaveable { mutableStateOf(intent) }
    var filename by rememberSaveable { mutableStateOf("") }
    var result: Result by remember { mutableStateOf(Result.RestoreFailed) }
    // openUri true on cold launch in case there's an Uri
    var openUri by rememberSaveable { mutableStateOf(true) }
    var showPreImportErrorDialog by rememberSaveable { mutableStateOf(false) }
    var showImportDialog by rememberSaveable { mutableStateOf(false) }
    var insertBackupIntoDb by rememberSaveable { mutableStateOf(false) }
    var showSuccessDialog by rememberSaveable { mutableStateOf(false) }
    // check new intent because MainActivity is SingleInstance
    DisposableEffect(Unit) {
        Log.d(TAG, "loadBackup() registering new intent listener")
        val listener = Consumer<Intent> {
            intentState = it
            openUri = true
        }
        activity.addOnNewIntentListener(listener)
        onDispose { activity.removeOnNewIntentListener(listener) }
    }
    LaunchedEffect(intentState) {
        if (openUri) {
            intentState?.data?.let { uri ->
                Log.d(TAG, "loadBackup() opening Uri")
                try {
                    var bytes: String?
                    runBlocking(Dispatchers.IO) {
                        bytes = activity.contentResolver.openInputStream(uri).use { fis ->
                            fis?.readBytes()!!.decodeToString()
                        }
                    }
                    dbBackup = Json.decodeFromString<DbBackup>(bytes!!)
                    if (dbBackup!!.isEmpty()) throw EmptyDatabaseException()
                    val documentFile = DocumentFile.fromSingleUri(activity, uri)
                    filename = documentFile?.name.toString()
                    Log.i(TAG, "loadBackup() Backup file is ready to be restored")
                    showImportDialog = true
                } catch (e: FileNotFoundException) {
                    Log.e(TAG, "loadBackup() Error opening file: $e")
                    result = Result.FileOpenError
                    showPreImportErrorDialog = true
                } catch (e: IllegalArgumentException) {
                    Log.e(TAG, "loadBackup() Error decoding file: $e")
                    result = Result.BadFile
                    showPreImportErrorDialog = true
                } catch (e: SerializationException) {
                    Log.e(TAG, "loadBackup() Error decoding file: $e")
                    result = Result.BadFile
                    showPreImportErrorDialog = true
                } catch (e: EmptyDatabaseException) {
                    Log.e(TAG, "loadBackup() $e")
                    result = Result.NothingToRestore
                    showPreImportErrorDialog = true
                } catch (e: Exception) {
                    Log.e(TAG, "loadBackup() Error opening file: $e")
                    result = Result.FileOpenError
                    showPreImportErrorDialog = true
                } finally {
                    intentState = null
                    openUri = false
                }
            }
        }
    }

    if (showPreImportErrorDialog) {
        Log.d(TAG, "loadBackup() Showing error dialog")
        AlertDialog(
            onDismissRequest = {
                openUri = false
                intentState = null
                dbBackup = null
                showPreImportErrorDialog = false
            }
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.secondary,
                )
            ) {
                Column(modifier = Modifier.padding(15.dp)) {
                    Text(
                        text = stringResource(id = R.string.import_data),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(
                            id = R.string.error_loading_backup,
                            filename,
                            stringResource(result.stringId)
                        ),
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {
                                openUri = false
                                intentState = null
                                dbBackup = null
                                showPreImportErrorDialog = false
                            }
                        ) {
                            Text(text = stringResource(id = R.string.accept))
                        }
                    }
                }
            }
        }
    }

    if (showImportDialog) {
        Log.d(TAG, "loadBackup() Showing import dialog, $dbBackup")
        dbBackup?.let { backupDb ->
            AlertDialog(
                onDismissRequest = {
                    openUri = false
                    intentState = null
                    dbBackup = null
                    showImportDialog = false
                }
            ) {
                val warning = stringResource(
                    id = if (backupDb.isLabelsOnly()) {
                        R.string.this_will_keep_current_data
                    } else {
                        R.string.warning_this_will_erase
                    }
                )
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.secondary,
                    )
                ) {
                    Column(modifier = Modifier.padding(15.dp)) {
                        Text(
                            text = stringResource(id = R.string.import_data),
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(
                                id = R.string.load_backup,
                                filename
                            ),
                        )
                        Spacer(modifier = Modifier.height(25.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = warning,
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = {
                                    openUri = false
                                    intentState = null
                                    dbBackup = null
                                    showImportDialog = false
                                }
                            ) {
                                Text(text = stringResource(id = R.string.cancel))
                            }
                            TextButton(
                                onClick = {
                                    openUri = false
                                    intentState = null
                                    insertBackupIntoDb = true
                                    showImportDialog = false
                                }
                            ) {
                                Text(text = stringResource(id = R.string.accept))
                            }
                        }
                    }
                }
            }
        }
    }

    if (insertBackupIntoDb) {
        Log.e(TAG, "loadBackup() Inserting backup into DB")
        dbBackup?.let { backup ->
            runBlocking {
                // only insert tags that do not exist already, as in
                // they have the same name and color regardless of id
                if (backup.persons.isNotEmpty()) launch {
                    val persons = appDao.getActivePersonsList().map { Pair(it.name, it.color) }
                    backup.persons.forEach {
                        val alreadyExists = persons.contains(Pair(it.name, it.color))
                        if (!alreadyExists) appDao.upsertPerson(it)
                    }
                }
                if (backup.places.isNotEmpty()) launch {
                    val places = appDao.getActivePlacesList().map { Pair(it.name, it.color) }
                    backup.places.forEach {
                        val alreadyExists = places.contains(Pair(it.name, it.color))
                        if (!alreadyExists) appDao.upsertPlace(it)
                    }
                }
                if (backup.tags.isNotEmpty()) launch {
                    val tags = appDao.getActiveTagsList().map { Pair(it.name, it.color) }
                    backup.tags.forEach {
                        val alreadyExists = tags.contains(Pair(it.name, it.color))
                        if (!alreadyExists) appDao.upsertTag(it)
                    }
                }
            }
            Log.i(TAG, "FromString() Restore of labels success")
            if (!backup.isLabelsOnly()) {
                runBlocking {
                    Log.i(TAG, "FromString() Deleting current data")
                    appDao.deleteAllData()
                    launch {
                        backup.preselectedPersons.forEach {
                            appDao.upsertPreSelectedPerson(it)
                        }
                    }
                    launch {
                        backup.preselectedPlaces.forEach {
                            appDao.upsertPreSelectedPlace(it)
                        }
                    }
                    launch {
                        backup.preselectedTags.forEach {
                            appDao.upsertPreSelectedTag(it)
                        }
                    }
                    launch { backup.events.forEach { appDao.upsertEvent(it) } }
                    launch { backup.sessions.forEach { appDao.upsertSession(it) } }
                    launch { backup.prefs.forEach { appDao.upsertPreference(it) } }
                }
                Log.i(TAG, "FromString() Restore of full backup success")
            }
            dbBackup = null
            showSuccessDialog = true
            insertBackupIntoDb = false
        }
    }

    if (showSuccessDialog) {
        Log.d(TAG, "loadBackup() Showing success dialog")
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false }
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.secondary,
                )
            ) {
                Column(modifier = Modifier.padding(15.dp)) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = Result.Restored.stringId),
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = { showSuccessDialog = false }
                        ) {
                            Text(text = stringResource(id = R.string.accept))
                        }
                    }
                }
            }
        }
    }
}

private const val TAG = "IntentProcessor"
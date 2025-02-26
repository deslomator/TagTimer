package com.deslomator.tagtimer.viewmodel

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.BackupAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.DbBackup
import com.deslomator.tagtimer.model.type.Backup
import com.deslomator.tagtimer.model.type.BackupButton
import com.deslomator.tagtimer.model.type.Result
import com.deslomator.tagtimer.state.BackupState
import com.deslomator.tagtimer.util.restoreBackup
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.FileReader
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val appDao: AppDao,
): ViewModel() {

    private val backupDir = File(context.filesDir, "backup")
    private val contentResolver = context.contentResolver
    private val _state = MutableStateFlow(BackupState())

    val state = _state
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BackupState())

    fun onAction(action: BackupAction) {
        when(action) {
            is BackupAction.DeleteBackupClicked -> {
                deleteFile(action.file)
            }
            is BackupAction.ShareBackupClicked -> {
                val reader = BufferedReader(FileReader(action.file))
                val string = reader.readText()
                _state.update {
                    it.copy(
                        currentString = string,
                        currentFile = action.file,
                        shareFile = true
                    )
                }
            }
            is BackupAction.TopButtonClicked -> {
                when (action.button) {
                    BackupButton.LABELS, BackupButton.FULL -> {
                        viewModelScope.launch {
                            val lbOnly = action.button == BackupButton.LABELS
                            val result = backupInternally(
                                labelsOnly = lbOnly,
                                appDao = appDao,
                                backupDir = backupDir,
                                file = null,
                            )
                            _state.update {
                                it.copy(
                                    result = result,
                                )
                            }
                            reloadFiles()
                        }
                    }
                    BackupButton.RESTORE -> {
                        _state.update { it.copy(loadFileFromStorage = true) }
                    }
                }
            }
            is BackupAction.RestoreBackupClicked -> {
                viewModelScope.launch {
                    val result = restoreBackup(appDao, Uri.fromFile(action.file))
                    _state.update {
                        it.copy(
                            result = result,
                        )
                    }
                }
            }
            is BackupAction.BackupShared -> {
                _state.update { it.copy(shareFile = false) }
            }
            is BackupAction.SaveBackupToStorageClicked -> {
                _state.update { it.copy(currentFile = action.file) }
            }
            is BackupAction.SaveToStorageUriReceived -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val result = saveToStorage(action.uri)
                    _state.update { it.copy(
                        result = result,
                    ) }
                }
            }
            is BackupAction.LoadFromStorageUriReceived -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val result = loadFromStorage(action.uri, action.tempFile)
                    _state.update { it.copy(
                        result = result,
                        loadFileFromStorage = false
                    ) }
                }
            }
            is BackupAction.LoadFromStorageDialogDismissed -> {
                _state.update { it.copy(
                    loadFileFromStorage = false,
                    result = Result.NothingRestored
                ) }
            }
        }
    }

    private fun deleteFile(file: File) {
        viewModelScope.launch(Dispatchers.IO) {
            file.delete()
            reloadFiles()
        }
        _state.update { it.copy(
            result = Result.Deleted,
        ) }
    }

    private fun loadFromStorage(uri: Uri, tempFile: File): Result {
        uri.let { contentResolver.openInputStream(it) }.use { input ->
            tempFile.outputStream().use { output ->
                input?.copyTo(output)
            }
        }
        return restoreBackup(appDao, Uri.fromFile(tempFile))
    }

    /**
     * using a byte array is a way of saving
     * the file to storage without it being truncated
     */
    private fun saveToStorage(uri: Uri): Result {
        var result: Result = Result.SaveFailed
        try {
            val outStream = contentResolver.openOutputStream(uri)
            if (outStream != null) {
                outStream.use { fos ->
                    FileInputStream(state.value.currentFile).use { inputStream ->
                        inputStream.readBytes()
                    }.inputStream().use { bis ->
                        val buf = ByteArray(bis.available())
                        bis.read(buf)
                        do {
                            fos.write(buf)
                        } while (bis.read(buf) != -1)
                    }
                }
                Log.i(TAG, "saveToStorage(). Save success")
                result = Result.Saved
            } else {
                Log.e(TAG, "saveToStorage(). Unable to open Output Stream")
            }
        } catch (e: Exception) {
            Log.e(TAG, "saveToStorage(). $e")
        }
        return result
    }

    private fun reloadFiles() {
        _state.update { it.copy(files = backupDir.listFiles()?.toList()?.sortedBy { f -> f.name } ?: emptyList()) }
    }

    init {
        backupDir.mkdir()
        reloadFiles()
    }

    companion object {
        private const val TAG = "BackupViewModel"
    }
}

private fun backupInternally(
    labelsOnly: Boolean = false,
    appDao: AppDao,
    backupDir: File,
    file: File? = null
): Result {
    var result: Result = Result.BackupFailed
    runBlocking(Dispatchers.IO) {
        val timestamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss", Locale.getDefault()
        ).format(Date())
        val prefix = if (labelsOnly) Backup.LABELS.type else Backup.FULL.type
        val fileName = "${prefix}_${timestamp}.json"
        val new = file ?: File(backupDir, fileName)
        if (new.createNewFile()) {
            val dbBackup = getDbBackup(appDao, labelsOnly)
            if (dbBackup.isEmpty()) {
                Log.e("backupInternally()", "Nothing to backup, database is empty")
                result = Result.NothingToBackup
            } else {
                try {
                    Json.encodeToString(dbBackup)
                        .encodeToByteArray()
                        .inputStream().use { bis ->
                            FileOutputStream(new).use { fos ->
                                val buf = ByteArray(bis.available())
                                bis.read(buf)
                                do {
                                    fos.write(buf)
                                } while (bis.read(buf) != -1)
                            }
                        }
                    result = Result.Backed
                    Log.i("backupInternally()", "Backup success")
                } catch (e: SerializationException) {
                    Log.e("backupInternally()", "Serialization failed: $e")
                } catch (e: IllegalArgumentException) {
                    Log.e("backupInternally()", "Serialization failed: $e")
                } catch (e: FileNotFoundException) {
                    Log.e("backupInternally()", "Could not save backup file: $e")
                    result = Result.SaveFailed
                } catch (e: Exception) {
                    Log.e("backupInternally()", e.message.toString())
                }
            }
        } else {
            Log.e("backupInternally()", "Failed, backup file already exists")
        }
    }
    return result
}

private suspend fun getDbBackup(appDao: AppDao, labelsOnly: Boolean): DbBackup {
    var dbBackup: DbBackup
    withContext(Dispatchers.IO) {
        when (labelsOnly) {
            true -> {
                val tags = async { appDao.getAllTagsList() }
                val places = async { appDao.getAllPlacesList() }
                val persons = async { appDao.getAllPersonsList() }
                dbBackup = DbBackup(
                    tags = tags.await(),
                    places = places.await(),
                    persons = persons.await(),
                )
            }
            false -> {
                val tags = async { appDao.getAllTagsList() }
                val places = async { appDao.getAllPlacesList() }
                val persons = async { appDao.getAllPersonsList() }
                val events = async { appDao.getAllEventsList() }
                val preselectedPersons = async { appDao.getAllPreselectedPersonsList() }
                val preselectedPlaces = async { appDao.getAllPreselectedPlacesList() }
                val preselectedTags = async { appDao.getAllPreselectedTagsList() }
                val sessions = async { appDao.getAllSessionsList() }
                val prefs = async { appDao.getAllPreferencesList() }
                dbBackup = DbBackup(
                    tags = tags.await(),
                    places = places.await(),
                    persons = persons.await(),
                    events = events.await(),
                    preselectedPersons = preselectedPersons.await(),
                    preselectedPlaces = preselectedPlaces.await(),
                    preselectedTags = preselectedTags.await(),
                    sessions = sessions.await(),
                    prefs = prefs.await()
                )
            }
        }
    }
    return dbBackup
}


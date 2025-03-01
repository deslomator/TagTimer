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
import com.deslomator.tagtimer.model.type.FileItemButton
import com.deslomator.tagtimer.model.type.Result
import com.deslomator.tagtimer.state.BackupState
import com.deslomator.tagtimer.util.restoreBackup
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
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
) : ViewModel() {

    private val backupDir = File(context.filesDir, "backup")
    private val contentResolver = context.contentResolver
    private val _state = MutableStateFlow(BackupState())

    val state = _state
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BackupState())

    fun onAction(action: BackupAction) {
        when (action) {
            is BackupAction.TopButtonClicked -> {
                topButtonAction(action.button)
            }

            is BackupAction.FileItemActionClicked -> {
                fileAction(action.button, action.file)
            }

            is BackupAction.BackupShared -> {
                _state.update {
                    it.copy(
                        shareFile = false,
                        result = action.result
                    )
                }
            }

            is BackupAction.SaveToStorageUriReceived -> {
                if (action.uri == null) {
                    _state.update {
                        it.copy(
                            saveFileToStorage = false,
                            result = Result.NothingSaved
                        )
                    }
                } else {
                    val result = saveToStorage(action.uri)
                    _state.update {
                        it.copy(
                            result = result,
                            saveFileToStorage = false
                        )
                    }
                }
            }

            is BackupAction.LoadFromStorageUriReceived -> {
                if (action.uri != null && action.tempFile != null) {
                    viewModelScope.launch(Dispatchers.IO) {
                        val result = loadFromStorage(action.uri, action.tempFile)
                        _state.update {
                            it.copy(
                                result = result,
                                loadFileFromStorage = false
                            )
                        }
                    }
                } else {
                    _state.update {
                        it.copy(
                            loadFileFromStorage = false,
                            result = Result.NothingRestored
                        )
                    }
                }
            }
        }
    }

    private fun topButtonAction(button: BackupButton) {
        when (button) {
            BackupButton.LABELS, BackupButton.FULL -> {
                viewModelScope.launch {
                    val lbOnly = button == BackupButton.LABELS
                    val result = backupInternally(
                        labelsOnly = lbOnly,
                        appDao = appDao,
                        backupDir = backupDir,
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

    private fun fileAction(button: FileItemButton, file: File) {
        when (button) {
            FileItemButton.RESTORE -> {
                viewModelScope.launch {
                    val result = restoreBackup(appDao, Uri.fromFile(file))
                    _state.update {
                        it.copy(
                            result = result,
                            restoreBackup = true
                        )
                    }
                    delay(100)
                    _state.update {
                        it.copy(restoreBackup = false)
                    }
                }
            }

            FileItemButton.SAVE_TO_STORAGE -> {
                _state.update {
                    it.copy(
                        currentFile = file,
                        saveFileToStorage = true
                    )
                }
            }

            FileItemButton.SHARE -> {
                val reader = BufferedReader(FileReader(file))
                val string = reader.readText()
                _state.update {
                    it.copy(
                        currentString = string,
                        currentFile = file,
                        shareFile = true
                    )
                }
            }

            FileItemButton.DELETE -> {
                viewModelScope.launch(Dispatchers.IO) {
                    file.delete()
                    reloadFiles()
                    _state.update {
                        it.copy(
                            result = Result.Deleted,
                            deleteBackup = true
                        )
                    }
                    delay(100)
                    _state.update { it.copy(deleteBackup = false) }
                }
            }
        }
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
        _state.update {
            it.copy(files = backupDir.listFiles()?.toList()?.sortedBy { f -> f.name }
                ?: emptyList())
        }
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
): Result {
    var result: Result = Result.BackupFailed
    runBlocking(Dispatchers.IO) {
        val timestamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss", Locale.getDefault()
        ).format(Date())
        val prefix = if (labelsOnly) Backup.LABELS.type else Backup.FULL.type
        val fileName = "${prefix}_${timestamp}.json"
        val file = File(backupDir, fileName)
        if (file.createNewFile()) {
            val dbBackup = getDbBackup(appDao, labelsOnly)
            if (dbBackup.isEmpty()) {
                Log.e("backupInternally()", "Nothing to backup, database is empty")
                result = Result.NothingToBackup
            } else {
                try {
                    Json.encodeToString(dbBackup)
                        .encodeToByteArray()
                        .inputStream().use { bis ->
                            FileOutputStream(file).use { fos ->
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
                val labels = async { appDao.getAllLabelsList() }
                dbBackup = DbBackup(
                    labels = labels.await(),
                )
            }

            false -> {
                val labels = async { appDao.getAllLabelsList() }
                val events = async { appDao.getAllEventsList() }
                val preselectedLabels = async { appDao.getAllPreselectedLabelsList() }
                val sessions = async { appDao.getAllSessionsList() }
                val prefs = async { appDao.getAllPreferencesList() }
                dbBackup = DbBackup(
                    labels = labels.await(),
                    events = events.await(),
                    preselected = preselectedLabels.await(),
                    sessions = sessions.await(),
                    prefs = prefs.await()
                )
            }
        }
    }
    return dbBackup
}


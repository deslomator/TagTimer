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
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileReader
import java.io.OutputStream
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
            is BackupAction.BackupLabelsClicked -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val result = backup(
                        labelsOnly = true,
                        appDao = appDao,
                        backupDir = backupDir,
                        file = null,
                    )
                    _state.update {
                        it.copy(
                            result = result,
                            showSnackBar = true
                        )
                    }
                    reloadFiles()
                }
            }
            is BackupAction.FullBackupClicked -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val result = backup(
                        labelsOnly = false,
                        appDao = appDao,
                        backupDir = backupDir,
                        file = null,
                    )
                    _state.update {
                        it.copy(
                            result = result,
                            showSnackBar = true
                        )
                    }
                    reloadFiles()
                }
            }
            is BackupAction.RestoreBackupClicked -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val result = restoreBackup(appDao, Uri.fromFile(action.file))
                    _state.update {
                        it.copy(
                            result = result,
                            showSnackBar = true
                        )
                    }
                }
            }
            is BackupAction.SnackbarShown -> {
                _state.update { it.copy(showSnackBar = false) }
            }
            BackupAction.BackupExported -> {
                _state.update { it.copy(shareFile = false) }
            }
            is BackupAction.SaveBackupClicked -> {
                _state.update { it.copy(currentFile = action.file) }
            }
            is BackupAction.UriReceived -> {
                val result = saveToStorage(action.uri)
                _state.update { it.copy(
                    result = result,
                    showSnackBar = true
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
            result = Result.DELETED,
            showSnackBar = true
        ) }
    }

    /**
     * using a byte array is a way of saving
     * the file to storage without it being truncated
     */
    private fun saveToStorage(uri: Uri): Result {
        var result = Result.SAVED
        viewModelScope.launch(Dispatchers.IO) {
            var fis: FileInputStream? = null
            var fos: OutputStream? = null
            try {
                fis = FileInputStream(state.value.currentFile)
                fos = contentResolver.openOutputStream(uri, "wt")
                val bis = fis.buffered()
                val bos = fos?.buffered()
                val length = fis.available()
                val buf = ByteArray(length)
                bis.read(buf)
                do {
                    bos?.write(buf)
                } while (bis.read(buf) != -1)
            } catch (error: Error) {
                Log.d(TAG, "UriReceived. $error")
                result = Result.SAVE_FAILED
            } finally {
                fos?.flush()
                fos?.close()
                fis?.close()
            }
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

private suspend fun backup(
    labelsOnly: Boolean = false,
    appDao: AppDao,
    backupDir: File,
    file: File? = null
): Result {
    var result = Result.BACKED
    withContext(Dispatchers.IO) {
        val timestamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss", Locale.getDefault()
        ).format(Date())
        val prefix = if (labelsOnly) Backup.LABELS.type else Backup.FULL.type
        val fileName = "${prefix}_${timestamp}.json"
        val new = file ?: File(backupDir, fileName)
        if (new.createNewFile()) {
            val full = getDbBackup(appDao, labelsOnly)
            if (full.isEmpty()) {
                result = Result.NOTHING_TO_BACKUP
            } else {
                var fis: ByteArrayInputStream? = null
                var fos: FileOutputStream? = null
                try {
                    val json = Json.encodeToString(full).encodeToByteArray()
                    fis = ByteArrayInputStream(json)
                    fos = FileOutputStream(new)
                    val bis = fis.buffered()
                    val bos = fos.buffered()
                    val length = fis.available()
                    val buf = ByteArray(length)
                    bis.read(buf)
                    do {
                        bos.write(buf)
                    } while (bis.read(buf) != -1)
                } catch (error: Error) {
                    Log.d("fullBackup()", error.message.toString())
                    result = Result.BACKUP_FAILED
                } finally {
                    fis?.close()
                    fos?.flush()
                    fos?.close()
                }
            }
        } else {
            result = Result.BACKUP_FAILED
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
                dbBackup = DbBackup(
                    tags = tags.await(),
                    places = places.await(),
                    persons = persons.await(),
                    events = events.await(),
                    preselectedPersons = preselectedPersons.await(),
                    preselectedPlaces = preselectedPlaces.await(),
                    preselectedTags = preselectedTags.await(),
                    sessions = sessions.await(),
                )
            }
        }
    }
    return dbBackup
}

fun DbBackup.isEmpty(): Boolean {
    return this.tags.isEmpty() &&
            this.places.isEmpty() &&
            this.persons.isEmpty() &&
            this.events.isEmpty() &&
            this.preselectedPersons.isEmpty() &&
            this.preselectedPlaces.isEmpty() &&
            this.preselectedTags.isEmpty() &&
            this.sessions.isEmpty()
}


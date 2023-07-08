package com.deslomator.tagtimer.viewmodel

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.BackupAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.dao.SessionsDatabase
import com.deslomator.tagtimer.model.BackedLabels
import com.deslomator.tagtimer.model.FullBackup
import com.deslomator.tagtimer.model.type.Backup
import com.deslomator.tagtimer.state.BackupState
import com.deslomator.tagtimer.model.type.Result
import com.deslomator.tagtimer.util.restoreFullBackup
import com.deslomator.tagtimer.util.restoreLabelsBackup
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val appDao: AppDao,
    private val database: SessionsDatabase
): ViewModel() {

    private val backupDir = File(context.filesDir, "backup")
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
                labelsBackup()
            }
            is BackupAction.FullBackupClicked -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val result = fullBackup(appDao, backupDir)
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
                restoreBackup(backupDir, action.file)
            }
            is BackupAction.SnackbarShown -> {
                _state.update { it.copy(showSnackBar = false) }
            }
            BackupAction.BackupExported -> {
                _state.update { it.copy(shareFile = false) }
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

    private fun labelsBackup() {
        val timestamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "${Backup.LABELS.type}__${timestamp}.json"
        val new = File(backupDir, fileName)
        val success = new.createNewFile()
        if (success) {
            viewModelScope.launch(Dispatchers.IO) {
                val labels = BackedLabels(
                    persons = appDao.getActivePersonsList().map { it.copy(
                        name = it.name,
                        color = it.color,
                    ) },
                    places = appDao.getActivePlacesList().map { it.copy(
                        name = it.name,
                        color = it.color
                    ) },
                    tags = appDao.getActiveTagsList().map { it.copy(
                        name = it.name,
                        color = it.color
                    ) },
                )
                if (
                    labels.persons.isNotEmpty() ||
                    labels.places.isNotEmpty() ||
                    labels.tags.isNotEmpty()
                ) {
                    val json = Json.encodeToString(labels)
                    val writer = BufferedWriter(FileWriter(new))
                    writer.write(json)
                    writer.close()
                    reloadFiles()
                    _state.update {
                        it.copy(
                            result = Result.BACKED,
                            showSnackBar = true
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            result = Result.NOTHING_TO_BACKUP,
                            showSnackBar = true
                        )
                    }
                }
            }
        } else {
            _state.update { it.copy(
                result = Result.BACKUP_FAILED,
                showSnackBar = true
            ) }
        }
    }

    private fun restoreBackup(backupDir: File, file: File) {
        var result: Result
        if (file.toString().contains(Backup.FULL.type)) {
            viewModelScope.launch(Dispatchers.IO) {
                // first backup current data in case file is bad
                val temp = File(backupDir, "tmp.json")
                result = fullBackup(appDao, backupDir, temp)
                if (result == Result.BACKED) {
                    //now that we have a backup, delete all records
                    database.clearAllTables()
                    result = restoreFullBackup(appDao, Uri.fromFile(file))
                    if (result == Result.RESTORED) {
                        _state.update {
                            it.copy(
                                result = result,
                                showSnackBar = true
                            )
                        }
                    } else { // restore the temporal backup in case of error
                        restoreFullBackup(appDao, Uri.fromFile(temp))
                        _state.update {
                            it.copy(
                                result = Result.RESTORE_FAILED,
                                showSnackBar = true
                            )
                        }
                    }
                }
                temp.delete()
            }
        } else if (file.toString().contains(Backup.LABELS.type)) {
            viewModelScope.launch(Dispatchers.IO) {
                result = restoreLabelsBackup(appDao, Uri.fromFile(file))
                _state.update { it.copy(
                    result = result,
                    showSnackBar = true
                ) }
            }
        } else {
            _state.update {
                it.copy(
                    result = Result.RESTORE_FAILED,
                    showSnackBar = true
                )
            }
        }
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

private suspend fun fullBackup(appDao: AppDao, backupDir: File, file: File? = null): Result {
    var result = Result.BACKED

    withContext(Dispatchers.IO) {
        val timestamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss", Locale.getDefault()
        ).format(Date())
        val fileName = "${Backup.FULL.type}_${timestamp}.json"
        val new = file ?: File(backupDir, fileName)
        if (new.createNewFile()) {
            val full = FullBackup(
                tags = appDao.getAllTagsList(),
                places = appDao.getAllPlacesList(),
                persons = appDao.getAllPersonsList(),
                events = appDao.getAllEventsList(),
                preselectedPersons = appDao.getAllPreselectedPersonsList(),
                preselectedPlaces = appDao.getAllPreselectedPlacesList(),
                preselectedTags = appDao.getAllPreselectedTagsList(),
                sessions = appDao.getAllSessionsList(),
            )
            if (
                full.tags.isNotEmpty() ||
                full.places.isNotEmpty() ||
                full.persons.isNotEmpty() ||
                full.events.isNotEmpty() ||
                full.preselectedPersons.isNotEmpty() ||
                full.preselectedPlaces.isNotEmpty() ||
                full.preselectedTags.isNotEmpty() ||
                full.sessions.isNotEmpty()
            ) {
                try {
                    val json = Json.encodeToString(full)
                    val writer = BufferedWriter(FileWriter(new))
                    writer.write(json)
                    writer.close()
                } catch (error: Error) {
                    Log.d("fullBackup()", error.message.toString())
                    result = Result.BACKUP_FAILED
                }
            } else {
                Result.NOTHING_TO_BACKUP
            }
        } else {
            result = Result.BACKUP_FAILED
        }
    }
    return result
}




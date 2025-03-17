package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.DbBackup
import com.deslomator.tagtimer.model.type.Result
import java.io.File

data class BackupState(
    val files: List<File> = emptyList(),
    val shareFile: Boolean = false,
    val loadFileFromStorage: Boolean = false,
    val saveFileToStorage: Boolean = false,
    val showSnackbar: Boolean = false,
    val showFullRestoreDialog: Boolean = false,
    val dbBackup: DbBackup? = null,
    val result: Result = Result.Restored,
    val currentString: String = "",
    val currentFile: File? = null
)


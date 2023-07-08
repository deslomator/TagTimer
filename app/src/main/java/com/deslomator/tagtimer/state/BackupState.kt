package com.deslomator.tagtimer.state

import java.io.File

data class BackupState(
    val files: List<File> = emptyList(),
    val currentFile: File? = null,
    val showSnackBar: Boolean = false,
    val result: Result = Result.RESTORED
)

enum class Result {
    BAD_FILE,
    RESTORED,
    DELETED,
    BACKED,
    BACKUP_FAILED,
    NOTHING_TO_BACKUP,
    NOTHING_TO_RESTORE,
    RESTORE_FAILED
}

enum class Backup(val type: String) {
    FULL("full"),
    LABELS("labels"),
}
package com.deslomator.tagtimer.model.type

import androidx.annotation.StringRes
import com.deslomator.tagtimer.R

sealed class Result(
    val name: String,
    @StringRes val stringId: Int,
) {
    object BadFile: Result("Bad File", R.string.bad_backup_file)
    object Restored: Result("Restored", R.string.backup_restored)
    object Deleted: Result("Deleted", R.string.backup_deleted)
    object Backed: Result("Backed", R.string.backup_saved)
    object BackupFailed: Result("Backup Failed", R.string.backup_failed)
    object NothingToBackup: Result("Nothing To Backup", R.string.nothing_to_backup)
    object NothingToRestore: Result("Nothing To Restore", R.string.nothing_to_restore)
    object RestoreFailed: Result("Restore Failed", R.string.restore_failed)
    object Saved: Result("Saved", R.string.backup_saved)
    object SaveFailed: Result("Save Failed", R.string.save_failed)
    object FileOpenError: Result("File Open Error", R.string.open_file_error)
}



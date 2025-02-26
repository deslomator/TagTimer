package com.deslomator.tagtimer.model.type

import androidx.annotation.StringRes
import com.deslomator.tagtimer.R

sealed class Result(
    val name: String,
    @StringRes val stringId: Int,
) {
    data object BadFile : Result("Bad File", R.string.bad_backup_file)
    data object Restored : Result("Restored", R.string.backup_restored)
    data object Deleted : Result("Deleted", R.string.backup_deleted)
    data object Backed : Result("Backed", R.string.backup_saved)
    data object BackupFailed : Result("Backup Failed", R.string.backup_failed)
    data object NothingToBackup : Result("Nothing To Backup", R.string.nothing_to_backup)
    data object NothingToRestore : Result("Nothing To Restore", R.string.nothing_to_restore)
    data object RestoreFailed : Result("Restore Failed", R.string.restore_failed)
    data object Saved : Result("Saved", R.string.backup_saved)
    data object SaveFailed : Result("Save Failed", R.string.save_failed)
    data object FileOpenError : Result("File Open Error", R.string.open_file_error)
    data object NothingRestored : Result("Nothing Restored", R.string.nothing_restored)
}



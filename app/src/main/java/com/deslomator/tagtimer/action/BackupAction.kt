package com.deslomator.tagtimer.action

import android.net.Uri
import com.deslomator.tagtimer.model.type.BackupButton
import com.deslomator.tagtimer.model.type.FileItemButton
import com.deslomator.tagtimer.model.type.Result
import java.io.File

sealed interface BackupAction {
    data class TopButtonClicked(val button: BackupButton) : BackupAction
    data class FileItemActionClicked(val button: FileItemButton, val file: File) : BackupAction
    data class SaveToStorageUriReceived(val uri: Uri?) : BackupAction
    data class LoadFromStorageUriReceived(val uri: Uri?, val tempFile: File?) : BackupAction
    data class BackupShared(val result: Result) : BackupAction
    data object SnackbarShown: BackupAction
    data object FullRestoreAccepted: BackupAction
    data object FullRestoreDismissed: BackupAction
}
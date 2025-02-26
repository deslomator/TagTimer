package com.deslomator.tagtimer.action

import android.net.Uri
import com.deslomator.tagtimer.model.type.BackupButton
import java.io.File

sealed interface BackupAction {
    class TopButtonClicked(val button: BackupButton) : BackupAction
    class DeleteBackupClicked(val file: File) : BackupAction
    class ShareBackupClicked(val file: File) : BackupAction
    class SaveBackupToStorageClicked(val file: File) : BackupAction
    class RestoreBackupClicked(val file: File) : BackupAction
    class SaveToStorageUriReceived(val uri: Uri) : BackupAction
    class LoadFromStorageUriReceived(val uri: Uri, val tempFile: File) : BackupAction
    data object LoadFromStorageDialogDismissed : BackupAction
    data object BackupShared : BackupAction
}
package com.deslomator.tagtimer.action

import android.net.Uri
import com.deslomator.tagtimer.model.type.BackupButton
import java.io.File

sealed interface BackupAction {
    class TopButtonClicked(val button: BackupButton) : BackupAction
    class DeleteBackupClicked(val file: File) : BackupAction
    class ShareBackupClicked(val file: File) : BackupAction
    class SaveBackupClicked(val file: File) : BackupAction
    class RestoreBackupClicked(val file: File) : BackupAction
    class UriReceived(val uri: Uri) : BackupAction
    data object BackupShared : BackupAction
}
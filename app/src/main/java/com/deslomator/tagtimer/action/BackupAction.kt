package com.deslomator.tagtimer.action

import android.net.Uri
import java.io.File

sealed interface BackupAction {
    data object FullBackupClicked: BackupAction
    data object BackupLabelsClicked: BackupAction
    class DeleteBackupClicked(val file: File): BackupAction
    class ShareBackupClicked(val file: File): BackupAction
    class SaveBackupClicked(val file: File): BackupAction
    class RestoreBackupClicked(val file: File): BackupAction
    class UriReceived(val uri: Uri): BackupAction
    data object BackupShared: BackupAction
}
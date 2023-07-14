package com.deslomator.tagtimer.action

import android.net.Uri
import java.io.File

sealed interface BackupAction {
    object FullBackupClicked: BackupAction
    object BackupLabelsClicked: BackupAction
    data class DeleteBackupClicked(val file: File): BackupAction
    data class ShareBackupClicked(val file: File): BackupAction
    data class SaveBackupClicked(val file: File): BackupAction
    data class RestoreBackupClicked(val file: File): BackupAction
    data class UriReceived(val uri: Uri): BackupAction
    object BackupExported: BackupAction
}
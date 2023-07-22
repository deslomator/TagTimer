package com.deslomator.tagtimer.action

import android.net.Uri
import java.io.File

sealed interface BackupAction {
    object FullBackupClicked: BackupAction
    object BackupLabelsClicked: BackupAction
    class DeleteBackupClicked(val file: File): BackupAction
    class ShareBackupClicked(val file: File): BackupAction
    class SaveBackupClicked(val file: File): BackupAction
    class RestoreBackupClicked(val file: File): BackupAction
    class UriReceived(val uri: Uri): BackupAction
    object BackupExported: BackupAction
}
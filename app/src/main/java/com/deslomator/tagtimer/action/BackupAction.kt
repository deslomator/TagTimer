package com.deslomator.tagtimer.action

import java.io.File

sealed interface BackupAction {
    object FullBackupClicked: BackupAction
    object BackupLabelsClicked: BackupAction
    data class DeleteBackupClicked(val file: File): BackupAction
    data class ShareBackupClicked(val file: File): BackupAction
    data class RestoreBackupClicked(val file: File): BackupAction
    object SnackbarShown: BackupAction
}
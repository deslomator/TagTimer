package com.deslomator.tagtimer.action

import android.net.Uri
import com.deslomator.tagtimer.model.type.BackupButton
import com.deslomator.tagtimer.model.type.FileItemButton
import com.deslomator.tagtimer.model.type.Result
import java.io.File

sealed interface BackupAction {
    class TopButtonClicked(val button: BackupButton) : BackupAction
    class FileItemActionClicked(val button: FileItemButton, val file: File) : BackupAction
    class SaveToStorageUriReceived(val uri: Uri?) : BackupAction
    class LoadFromStorageUriReceived(val uri: Uri?, val tempFile: File?) : BackupAction
    class BackupShared(val result: Result) : BackupAction
}
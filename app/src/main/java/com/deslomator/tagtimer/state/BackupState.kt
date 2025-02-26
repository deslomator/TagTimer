package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.type.Result
import java.io.File

data class BackupState(
    val files: List<File> = emptyList(),
    val shareFile: Boolean = false,
    val loadFileFromStorage: Boolean = false,
    val result: Result = Result.Restored,
    val currentString: String = "",
    val currentFile: File? = null
)


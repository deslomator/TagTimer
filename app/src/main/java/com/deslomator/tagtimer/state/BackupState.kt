package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.type.Result
import java.io.File

data class BackupState(
    val files: List<File> = emptyList(),
    val showSnackBar: Boolean = false,
    val result: Result = Result.RESTORED
)


package com.deslomator.tagtimer.model.type

import androidx.annotation.Keep

@Keep
enum class Result {
    BAD_FILE,
    RESTORED,
    DELETED,
    BACKED,
    BACKUP_FAILED,
    NOTHING_TO_BACKUP,
    NOTHING_TO_RESTORE,
    RESTORE_FAILED
}
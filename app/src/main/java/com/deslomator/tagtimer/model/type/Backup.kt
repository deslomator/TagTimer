package com.deslomator.tagtimer.model.type

import androidx.annotation.Keep

@Keep
enum class Backup(val type: String) {
    FULL("full"),
    LABELS("labels"),
}
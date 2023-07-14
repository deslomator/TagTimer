package com.deslomator.tagtimer.model.type

import androidx.annotation.Keep

/**
 * Types of objects that can
 * be shown in labels screen
 */
@Keep
sealed class Shared(
    val extension: String,
    val mime: String,
) {
    object Json: Shared("json", "application/json")
    object Csv: Shared("csv", "application/csv")
}
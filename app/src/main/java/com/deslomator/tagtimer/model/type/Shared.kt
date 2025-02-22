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
    data object Json: Shared("json", "application/json")
    data object Csv: Shared("csv", "application/csv")
}
package com.deslomator.tagtimer.model.type

import androidx.annotation.Keep
import com.deslomator.tagtimer.R

/**
 * Types of label sorting
 */
@Keep
enum class SessionSort(val stringId: Int) {
    DATE(R.string.sort_by_session_date),
    LAST_ACCESS(R.string.sort_by_last_access),
    NAME(R.string.sort_by_session_name)
}
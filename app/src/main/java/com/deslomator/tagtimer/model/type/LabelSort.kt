package com.deslomator.tagtimer.model.type

import androidx.annotation.Keep
import com.deslomator.tagtimer.R

/**
 * Types of label sorting
 */
@Keep
enum class LabelSort(val stringId: Int) {
    COLOR(R.string.sort_by_color),
    NAME(R.string.sort_by_name)
}
package com.deslomator.tagtimer.model.type

import androidx.annotation.Keep
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.model.COLUMN_LABEL_COLOR
import com.deslomator.tagtimer.model.COLUMN_LABEL_NAME

/**
 * Types of label sorting
 */
@Keep
enum class LabelSort(val stringId: Int, val sortId: String) {
    COLOR(R.string.sort_by_color, COLUMN_LABEL_COLOR),
    NAME(R.string.sort_by_name, COLUMN_LABEL_NAME)
}
package com.deslomator.tagtimer.model.type

import androidx.annotation.Keep
import com.deslomator.tagtimer.R

/**
 * Types of label sorting
 */
@Keep
enum class LabelType(val typeId: Int, val iconId: Int) {
    TAG(0, R.drawable.tag),
    PERSON(1, R.drawable.person),
    PLACE(2, R.drawable.place)
}
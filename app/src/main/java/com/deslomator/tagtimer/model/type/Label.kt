package com.deslomator.tagtimer.model.type

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.deslomator.tagtimer.R

/**
 * Types of objects that can
 * be shown in labels screen
 */
sealed class Label(
    @StringRes val stringId: Int,
    @DrawableRes val icon: Int,
) {
    object Tag: Label(R.string.tags, R.drawable.tag)
    object Person: Label(R.string.persons, R.drawable.person)
    object Place: Label(R.string.places, R.drawable.place)
}
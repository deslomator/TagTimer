package com.deslomator.tagtimer.model.type

import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.deslomator.tagtimer.R

/**
 * Types of objects that can
 * be shown in labels screen
 */
@Keep
sealed class LabelScreen(
    @StringRes val stringId: Int,
    @DrawableRes val icon: Int,
) {
    object Tag: LabelScreen(R.string.tags, R.drawable.tag)
    object Person: LabelScreen(R.string.persons, R.drawable.person)
    object Place: LabelScreen(R.string.places, R.drawable.place)
}
package com.deslomator.tagtimer.model.type

import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.deslomator.tagtimer.R

/**
 * Types of objects that can
 * be shown in trash screen
 */
@Keep
sealed class Trash(
    @StringRes val stringId: Int,
    @DrawableRes val icon: Int,
) {
    data object Session: Trash(R.string.sessions, R.drawable.document_and_ray)
    data object Tag: Trash(R.string.tags, R.drawable.tag)
    data object Person: Trash(R.string.persons, R.drawable.person)
    data object Place: Trash(R.string.places, R.drawable.place)
}
package com.deslomator.tagtimer.navigation.screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.deslomator.tagtimer.R

sealed class BottomNavigationScreen(
    val route: String,
    @StringRes val stringId: Int,
    @DrawableRes val icon: Int,
) {
    data object Sessions : BottomNavigationScreen("sessions", R.string.sessions, R.drawable.document_and_ray)
    data object Labels : BottomNavigationScreen("labels", R.string.labels, R.drawable.tag)
    data object Trash : BottomNavigationScreen("trash", R.string.trash, R.drawable.delete)
}

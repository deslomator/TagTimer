package com.deslomator.tagtimer.ui.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.deslomator.tagtimer.R

sealed class BottomNavigationScreen(
    val route: String,
    @StringRes val stringId: Int,
    @DrawableRes val icon: Int,
) {
    object Sessions : BottomNavigationScreen("sessions", R.string.sessions, R.drawable.document_and_ray)
    object Labels : BottomNavigationScreen("labels", R.string.labels, R.drawable.tag)
    object Trash : BottomNavigationScreen("trash", R.string.trash, R.drawable.delete)
}

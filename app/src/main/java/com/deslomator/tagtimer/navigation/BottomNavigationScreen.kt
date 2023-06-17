package com.deslomator.tagtimer.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.deslomator.tagtimer.R

sealed class BottomNavigationScreen(
    val route: String,
    @StringRes val stringId: Int,
    @DrawableRes val icon: Int,
) {
    object Main : BottomNavigationScreen(
        "main",
        R.string.app_name,
        R.drawable.baseline_edit_24,
    )
    object Sessions : BottomNavigationScreen("sessions", R.string.sessions, R.drawable.baseline_playlist_add_24)
    object Tags : BottomNavigationScreen("tags", R.string.tags, R.drawable.baseline_label_24)
    object Trash : BottomNavigationScreen("trash", R.string.trash, R.drawable.baseline_delete_24)
}

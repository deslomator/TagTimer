package com.deslomator.tagtimer.navigation.screen

import androidx.annotation.StringRes
import com.deslomator.tagtimer.R

sealed class RootScreen(
    val route: String,
    @StringRes val stringId: Int,
) {
    object Main : RootScreen("main", R.string.app_name)
    object Backup : RootScreen("backup_screen", R.string.full_backup)
}

package com.deslomator.tagtimer.ui.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.deslomator.tagtimer.R

sealed class RootScreen(
    val route: String,
    @StringRes val stringId: Int,
) {
    object Main : RootScreen("main", R.string.app_name)
    object Active : RootScreen("active", R.string.active_session)
}

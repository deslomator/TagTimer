package com.deslomator.tagtimer.ui.active

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.deslomator.tagtimer.R

sealed class ActiveNavigationScreen(
    val route: String,
    @StringRes val stringId: Int,
    @DrawableRes val icon: Int,
) {
    object ActiveSession : ActiveNavigationScreen("ActiveSession", R.string.session, R.drawable.document_and_ray)
    object EventFilter : ActiveNavigationScreen("EventFilter", R.string.filter_events, R.drawable.filter)
}

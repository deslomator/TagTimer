package com.deslomator.tagtimer.navigation.screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.deslomator.tagtimer.R

sealed class ActiveNavigationScreen(
    val route: String,
    @StringRes val stringId: Int,
    @DrawableRes val icon: Int,
) {
    object ActiveSession : ActiveNavigationScreen("ActiveSession", R.string.session, R.drawable.document_and_ray)
    object LabelSelection : ActiveNavigationScreen("LabelSelection", R.string.select_labels, R.drawable.add_tag)
    object EventFilter : ActiveNavigationScreen("EventFilter", R.string.filter_events, R.drawable.filter)
    object EventTrash : ActiveNavigationScreen("EventTrash", R.string.trash, R.drawable.delete)
}

package com.deslomator.tagtimer.navigation.screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.deslomator.tagtimer.R

sealed class ActiveScreen(
     private val _route: String,
    @StringRes val stringId: Int,
    @DrawableRes val icon: Int,
) {
    private val argument = "{sessionId}"
    data object ActiveSession : ActiveScreen("ActiveSession", R.string.session, R.drawable.document_and_ray)
    data object LabelSelection : ActiveScreen("LabelSelection", R.string.select_labels, R.drawable.add_tag)
    data object EventFilter : ActiveScreen("EventFilter", R.string.filter_events, R.drawable.filter)
    data object EventTrash : ActiveScreen("EventTrash", R.string.trash, R.drawable.delete)

    val route = "$_route/$argument"
    fun routeWithArg(sessionId: Long) = "$_route/$sessionId"
}

package com.deslomator.tagtimer.navigation.screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.deslomator.tagtimer.R

sealed class ActiveScreen(
     private val _route: String,
    @StringRes val stringId: Int,
    @DrawableRes val icon: Int,
) {
    val argumant = "{sessionId}"
    object ActiveSession : ActiveScreen("ActiveSession", R.string.session, R.drawable.document_and_ray)
    object LabelSelection : ActiveScreen("LabelSelection", R.string.select_labels, R.drawable.add_tag)
    object EventFilter : ActiveScreen("EventFilter", R.string.filter_events, R.drawable.filter)
    object EventTrash : ActiveScreen("EventTrash", R.string.trash, R.drawable.delete)

    val route = "$_route/$argumant"
    fun routeWithArg(sessionId: Int) = "$_route/$sessionId"
}

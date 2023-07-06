package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.type.Sort

sealed interface SharedAction {
    data class TagSortClicked(val tagSort: Sort): SharedAction
    data class PersonSortClicked(val personSort: Sort): SharedAction
    data class PlaceSortClicked(val placeSort: Sort): SharedAction
    data class SetCursor(val cursor: Long): SharedAction
    object PlayPauseClicked: SharedAction
    object StopSession: SharedAction
}
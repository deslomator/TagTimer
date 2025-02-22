package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.type.Sort

sealed interface SharedAction {
    class TagSortClicked(val tagSort: Sort): SharedAction
    class PersonSortClicked(val personSort: Sort): SharedAction
    class PlaceSortClicked(val placeSort: Sort): SharedAction
    class SetCursor(val cursor: Long): SharedAction
    data object PlayPauseClicked: SharedAction
    data object StopTimer: SharedAction
}
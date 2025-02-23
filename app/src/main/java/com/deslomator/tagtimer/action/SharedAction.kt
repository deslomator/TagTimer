package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.type.Sort

sealed interface SharedAction {
    class TagSortClicked(val tagSort: Sort): SharedAction
    class PersonSortClicked(val personSort: Sort): SharedAction
    class PlaceSortClicked(val placeSort: Sort): SharedAction
}
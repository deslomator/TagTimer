package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.type.LabelSort

sealed interface SharedAction {
    class TagSortClicked(val tagSort: LabelSort): SharedAction
    class PersonSortClicked(val personSort: LabelSort): SharedAction
    class PlaceSortClicked(val placeSort: LabelSort): SharedAction
}
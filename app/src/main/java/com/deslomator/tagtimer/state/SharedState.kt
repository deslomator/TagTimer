package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.type.LabelSort

data class SharedState(
    val tagSort: LabelSort = LabelSort.COLOR,
    val personSort: LabelSort = LabelSort.NAME,
    val placeSort: LabelSort = LabelSort.NAME,
)

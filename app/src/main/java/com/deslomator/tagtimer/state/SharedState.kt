package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.type.Sort

data class SharedState(
    val tagSort: Sort = Sort.COLOR,
    val personSort: Sort = Sort.NAME,
    val placeSort: Sort = Sort.NAME,
    val cursor: Long = 0,
    val isRunning: Boolean = false
)

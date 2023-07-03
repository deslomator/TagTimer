package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Session

data class TrashTabState(
    val sessions: List<Session> = emptyList(),
    val tags: List<Label.Tag> = emptyList(),
    val persons: List<Label.Person> = emptyList(),
    val places: List<Label.Place> = emptyList(),
)

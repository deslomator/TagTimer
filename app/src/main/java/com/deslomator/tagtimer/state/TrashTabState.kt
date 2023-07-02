package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Lbl
import com.deslomator.tagtimer.model.Session

data class TrashTabState(
    val sessions: List<Session> = emptyList(),
    val tags: List<Lbl.Tag> = emptyList(),
    val persons: List<Lbl.Person> = emptyList(),
    val places: List<Lbl.Place> = emptyList(),
)

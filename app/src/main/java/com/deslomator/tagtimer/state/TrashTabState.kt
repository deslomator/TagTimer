package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Person
import com.deslomator.tagtimer.model.Place
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.Tag
import com.deslomator.tagtimer.model.type.Trash

data class TrashTabState(
    val sessions: List<Session> = emptyList(),
    val tags: List<Tag> = emptyList(),
    val persons: List<Person> = emptyList(),
    val places: List<Place> = emptyList(),
)

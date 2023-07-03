package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Preselected
import com.deslomator.tagtimer.model.Session

data class LabelPreselectionState(
    val preSelectedPersons: List<Preselected.Person> = emptyList(),
    val preSelectedPlaces: List<Preselected.Place> = emptyList(),
    val preSelectedTags: List<Preselected.Tag> = emptyList(),
    val tags: List<Label.Tag> = emptyList(),
    val persons: List<Label.Person> = emptyList(),
    val places: List<Label.Place> = emptyList(),
    val currentSession: Session = Session(),
)

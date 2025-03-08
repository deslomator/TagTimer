package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.type.DialogState
import com.deslomator.tagtimer.model.ancillary.PreferenceProvider

data class LabelSelectionState(
    val selectedPersons: List<Label> = emptyList(),
    val selectedPlaces: List<Label> = emptyList(),
    val selectedTags: List<Label> = emptyList(),
    val tags: List<Label> = emptyList(),
    val persons: List<Label> = emptyList(),
    val places: List<Label> = emptyList(),

    val currentSession: Session = Session(),

    val currentLabel: Label = Label(),
    val canBeDeleted: Boolean = false,
    val dialogState: DialogState = DialogState.HIDDEN,

    val preferenceProvider: PreferenceProvider = PreferenceProvider()
)

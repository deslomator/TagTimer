package com.deslomator.tagtimer.state

import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.type.DialogState
import com.deslomator.tagtimer.model.type.LabelSort

data class LabelPreselectionState(
    val preSelectedPersons: List<Label> = emptyList(),
    val preSelectedPlaces: List<Label> = emptyList(),
    val preSelectedTags: List<Label> = emptyList(),
    val tags: List<Label> = emptyList(),
    val persons: List<Label> = emptyList(),
    val places: List<Label> = emptyList(),
    val tagSort: LabelSort = LabelSort.COLOR,
    val personSort: LabelSort = LabelSort.NAME,
    val placeSort: LabelSort = LabelSort.NAME,
    val currentSession: Session = Session(),

    val currentLabel: Label = Label(),
    val dialogState: DialogState = DialogState.HIDDEN,
)

package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.EventAction
import com.deslomator.tagtimer.dao.EventDao
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.state.EventState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EventViewModel(private val dao: EventDao): ViewModel() {

    private val _state = MutableStateFlow(EventState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), EventState())

    fun onAction(action: EventAction) {
        when(action) {
            is EventAction.DeleteEvent -> {
                viewModelScope.launch { dao.deleteEvent(action.event) }
            }
            is EventAction.NoteEdited -> {
                val event = Event(
                    id = action.event.id,
                    sessionId = action.event.sessionId,
                    timestampMillis = action.event.timestampMillis,
                    category = action.event.category,
                    label = action.event.label,
                    color = action.event.color,
                    note = _state.value.note
                )
                viewModelScope.launch { dao.upsertEvent(event) }
                _state.update { it.copy(
                    note = "",
                    isEditingNote = false) }
            }
            is EventAction.AppendEvent -> {
                val event = Event(
                    sessionId = action.sessionId,
                    timestampMillis = action.timestamp,
                    category = action.tag.category,
                    label = action.tag.label,
                    color = action.tag.color
                )
                viewModelScope.launch { dao.upsertEvent(event) }
            }
            is EventAction.UpdateNote -> {
                _state.update { it.copy(note = action.noteText) }
            }
            is EventAction.EditNote -> {
                _state.update { it.copy(
                    editingIndex = action.editingIndex,
                    note = action.event.note,
                    isEditingNote = true
                ) }
            }
        }
    }
}
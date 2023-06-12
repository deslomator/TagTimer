package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.Action
import com.deslomator.tagtimer.dao.Dao
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.Tag
import com.deslomator.tagtimer.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(private val dao: Dao): ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState())

    fun onAction(action: Action) {
        when(action) {
            is Action.DeleteEvent -> {
                viewModelScope.launch { dao.deleteEvent(action.event) }
            }
            is Action.EventNoteEdited -> {
                val event = Event(
                    id = action.event.id,
                    sessionId = action.event.sessionId,
                    timestampMillis = action.event.timestampMillis,
                    tagId = action.event.tagId,
                    note = state.value.eventNote
                )
                viewModelScope.launch { dao.upsertEvent(event) }
                _state.update { it.copy(
                    eventNote = "",
                    isEditingEventNote = false) }
            }
            is Action.AppendEvent -> {
                val event = Event(
                    sessionId = action.sessionId,
                    timestampMillis = action.timestamp,
                    tagId = action.tagId
                )
                viewModelScope.launch { dao.upsertEvent(event) }
            }
            is Action.UpdateEventNote -> {
                _state.update { it.copy(eventNote = action.noteText) }
            }
            is Action.EditEventNote -> {
                _state.update { it.copy(
                    eventEditingIndex = action.editingIndex,
                    eventNote = action.event.note,
                    isEditingEventNote = true
                ) }
            }
            is Action.HideDeleteEventDialog -> {
                _state.update { it.copy(showEventDeleteDialog = false) }
            }
            is Action.ShowDeleteEventDialog -> {
                _state.update { it.copy(showEventDeleteDialog = true) }
            }
            Action.AddSession -> {
                _state.update { it.copy(isEditingSession = true) }
            }
            is Action.DeleteSession -> {
                viewModelScope.launch {
                    dao.deleteSession(action.session)
                    dao.deleteEventsForSession(action.session.id)
                }
            }
            is Action.EditSession -> {
                _state.update { it.copy(
                    sessionEditingIndex = action.editingIndex,
                    isEditingSession = true,
                    lastAccessMillis = System.currentTimeMillis(),
                    sessionColor = action.session.color,
                    sessionName = action.session.name
                ) }
            }
            is Action.UpsertSession -> {
                TODO()
            }
            is Action.UpdateSessionColor -> {
                _state.update { it.copy(sessionColor = action.color) }
            }
            is Action.UpdateSessionName -> {
                _state.update { it.copy(sessionName = action.name) }
            }
            is Action.SessionEdited -> {
                val session = Session(
                    id = action.session.id,
                    startTimeMillis = action.session.startTimeMillis,
                    endTimeMillis = action.session.endTimeMillis,
                    name = state.value.sessionName,
                    color = state.value.sessionColor,
                    lastAccessMillis = System.currentTimeMillis()
                )
                _state.update { it.copy(
                    isEditingSession = false,
                    sessionColor = 0,
                    sessionName = ""
                ) }
                viewModelScope.launch { dao.upsertSession(session) }
            }
            is Action.SessionAdded -> {
                val session = Session(
                    name = state.value.sessionName,
                    color = state.value.sessionColor,
                    lastAccessMillis = System.currentTimeMillis()
                )
                _state.update { it.copy(
                    isEditingSession = false,
                    sessionColor = 0,
                    sessionName = ""
                ) }
                viewModelScope.launch { dao.upsertSession(session) }
            }
            Action.HideDeleteSessionDialog -> {
                _state.update { it.copy(showSessionDeleteDialog = false) }
            }
            Action.ShowDeleteSessionDialog -> {
                _state.update { it.copy(showSessionDeleteDialog = true) }
            }
            is Action.DeleteTag -> {
                viewModelScope.launch { dao.deleteTag(action.tag) }
            }
            is Action.TagEdited -> {
                val tag = Tag(
                    id = action.tag.id,
                    category = state.value.tagCategory,
                    label = state.value.tagLabel,
                    color = state.value.tagColor
                )
                viewModelScope.launch { dao.upsertTag(tag) }
                _state.update { it.copy(
                    tagCategory = "",
                    tagLabel = "",
                    tagColor = 0,
                    isEditingTag = false) }
            }
            is Action.AddTag -> {
                _state.update { it.copy(isEditingTag = true) }
            }
            is Action.TagAdded -> {
                val tag = Tag(
                    label = state.value.tagLabel,
                    color = state.value.tagColor,
                    category = state.value.tagCategory
                )
                _state.update { it.copy(
                    isEditingTag = false,
                    tagColor = 0,
                    tagCategory = "",
                    tagLabel = ""
                ) }
                viewModelScope.launch { dao.upsertTag(tag) }
            }
            is Action.UpdateTagLabel -> {
                _state.update { it.copy(tagLabel = action.label) }
            }
            is Action.EditTag -> {
                _state.update { it.copy(
                    tagEditingIndex = action.editingIndex,
                    tagLabel = action.tag.label,
                    tagColor = action.tag.color,
                    tagCategory = action.tag.category,
                    isEditingTag = true
                ) }
            }
            is Action.HideDeleteTagDialog -> {
                _state.update { it.copy(showDeleteTagDialog = false) }
            }
            is Action.ShowDeleteTagDialog -> {
                _state.update { it.copy(showDeleteTagDialog = true) }
            }

            is Action.UpdateTagCategory -> {
                _state.update { it.copy(tagCategory = action.category) }
            }
            is Action.UpdateTagColor -> {
                _state.update { it.copy(tagColor = action.color) }
            }
            is Action.UpsertTag -> {
                viewModelScope.launch { dao.upsertTag(action.tag) }
            }
            is Action.UpsertUsedTag -> {
                viewModelScope.launch { dao.upsertUsedTag(action.usedTag) }
            }
            is Action.DeleteUsedTag -> {
                viewModelScope.launch { dao.deleteUsedTag(action.usedTag) }
            }
            is Action.ShowUsedTagDeleteDialog -> {
                _state.update { it.copy(showDeleteUsedTagDialog = true) }
            }
            is Action.HideUsedTagDeleteDialog -> {
                _state.update { it.copy(showDeleteUsedTagDialog = false) }
            }
            
            
        }
    }
}
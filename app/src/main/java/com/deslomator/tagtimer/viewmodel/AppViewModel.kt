package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.AppAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.Tag
import com.deslomator.tagtimer.state.AppState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(private val appDao: AppDao): ViewModel() {

    private val _state = MutableStateFlow(AppState())
    private val _sessions = appDao.getSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val state = combine(_state, _sessions) { state, sessions ->
        state.copy(
            sessions = sessions
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppState())

    fun onAction(appAction: AppAction) {
        when(appAction) {
            is AppAction.DeleteEvent -> {
                viewModelScope.launch { appDao.deleteEvent(appAction.event) }
            }
            is AppAction.EventNoteEdited -> {
                val event = Event(
                    id = appAction.event.id,
                    sessionId = appAction.event.sessionId,
                    timestampMillis = appAction.event.timestampMillis,
                    tagId = appAction.event.tagId,
                    note = state.value.eventNote
                )
                viewModelScope.launch { appDao.upsertEvent(event) }
                _state.update { it.copy(
                    eventNote = "",
                    isEditingEventNote = false) }
            }
            is AppAction.AppendEvent -> {
                val event = Event(
                    sessionId = appAction.sessionId,
                    timestampMillis = appAction.timestamp,
                    tagId = appAction.tagId
                )
                viewModelScope.launch { appDao.upsertEvent(event) }
            }
            is AppAction.UpdateEventNote -> {
                _state.update { it.copy(eventNote = appAction.noteText) }
            }
            is AppAction.EditEventNote -> {
                _state.update { it.copy(
                    eventEditingIndex = appAction.editingIndex,
                    eventNote = appAction.event.note,
                    isEditingEventNote = true
                ) }
            }
            is AppAction.HideDeleteEventDialog -> {
                _state.update { it.copy(showEventDeleteDialog = false) }
            }
            is AppAction.ShowDeleteEventDialog -> {
                _state.update { it.copy(showEventDeleteDialog = true) }
            }
            AppAction.AddSession -> {
                _state.update { it.copy(isEditingSession = true) }
            }
            is AppAction.DeleteSession -> {
                viewModelScope.launch {
                    appDao.deleteSession(appAction.session)
                    appDao.deleteEventsForSession(appAction.session.id)
                }
            }
            is AppAction.EditSession -> {
                _state.update { it.copy(
                    sessionEditingIndex = appAction.editingIndex,
                    isEditingSession = true,
                    lastAccessMillis = System.currentTimeMillis(),
                    sessionColor = appAction.session.color,
                    sessionName = appAction.session.name
                ) }
            }
            is AppAction.UpsertSession -> {
                TODO()
            }
            is AppAction.UpdateSessionColor -> {
                _state.update { it.copy(sessionColor = appAction.color) }
            }
            is AppAction.UpdateSessionName -> {
                _state.update { it.copy(sessionName = appAction.name) }
            }
            is AppAction.SessionEdited -> {
                val session = Session(
                    id = appAction.session.id,
                    startTimeMillis = appAction.session.startTimeMillis,
                    endTimeMillis = appAction.session.endTimeMillis,
                    name = state.value.sessionName,
                    color = state.value.sessionColor,
                    lastAccessMillis = System.currentTimeMillis()
                )
                _state.update { it.copy(
                    isEditingSession = false,
                    sessionColor = 0,
                    sessionName = ""
                ) }
                viewModelScope.launch { appDao.upsertSession(session) }
            }
            is AppAction.SessionAdded -> {
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
                viewModelScope.launch { appDao.upsertSession(session) }
            }
            AppAction.HideDeleteSessionDialog -> {
                _state.update { it.copy(showSessionDeleteDialog = false) }
            }
            AppAction.ShowDeleteSessionDialog -> {
                _state.update { it.copy(showSessionDeleteDialog = true) }
            }
            is AppAction.DeleteTag -> {
                viewModelScope.launch { appDao.deleteTag(appAction.tag) }
            }
            is AppAction.TagEdited -> {
                val tag = Tag(
                    id = appAction.tag.id,
                    category = state.value.tagCategory,
                    label = state.value.tagLabel,
                    color = state.value.tagColor
                )
                viewModelScope.launch { appDao.upsertTag(tag) }
                _state.update { it.copy(
                    tagCategory = "",
                    tagLabel = "",
                    tagColor = 0,
                    isEditingTag = false) }
            }
            is AppAction.AddTag -> {
                _state.update { it.copy(isEditingTag = true) }
            }
            is AppAction.TagAdded -> {
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
                viewModelScope.launch { appDao.upsertTag(tag) }
            }
            is AppAction.UpdateTagLabel -> {
                _state.update { it.copy(tagLabel = appAction.label) }
            }
            is AppAction.EditTag -> {
                _state.update { it.copy(
                    tagEditingIndex = appAction.editingIndex,
                    tagLabel = appAction.tag.label,
                    tagColor = appAction.tag.color,
                    tagCategory = appAction.tag.category,
                    isEditingTag = true
                ) }
            }
            is AppAction.HideDeleteTagDialog -> {
                _state.update { it.copy(showDeleteTagDialog = false) }
            }
            is AppAction.ShowDeleteTagDialog -> {
                _state.update { it.copy(showDeleteTagDialog = true) }
            }

            is AppAction.UpdateTagCategory -> {
                _state.update { it.copy(tagCategory = appAction.category) }
            }
            is AppAction.UpdateTagColor -> {
                _state.update { it.copy(tagColor = appAction.color) }
            }
            is AppAction.UpsertTag -> {
                viewModelScope.launch { appDao.upsertTag(appAction.tag) }
            }
            is AppAction.UpsertUsedTag -> {
                viewModelScope.launch { appDao.upsertUsedTag(appAction.usedTag) }
            }
            is AppAction.DeleteUsedTag -> {
                viewModelScope.launch { appDao.deleteUsedTag(appAction.usedTag) }
            }
            is AppAction.ShowUsedTagDeleteDialog -> {
                _state.update { it.copy(showDeleteUsedTagDialog = true) }
            }
            is AppAction.HideUsedTagDeleteDialog -> {
                _state.update { it.copy(showDeleteUsedTagDialog = false) }
            }
            
            
        }
    }
}
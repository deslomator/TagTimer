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
    private val _events = appDao.getEventsForSession(_state.value.currentSession.id)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _sessions = appDao.getSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _tags = appDao.getTags()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _usedTags = appDao.getUsedTagsForSession(_state.value.currentSession.id)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val state = combine(_state, _events, _sessions, _tags, _usedTags) { state, events, sessions, tags, usedTags ->
        state.copy(
            events = events,
            sessions = sessions,
            tags = tags,
            usedTags = usedTags
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppState())

    fun onAction(action: AppAction) {
        when(action) {
            is AppAction.DeleteEvent -> {
                viewModelScope.launch { appDao.deleteEvent(action.event) }
            }
            is AppAction.EventNoteEdited -> {
                val event = Event(
                    id = action.event.id,
                    sessionId = action.event.sessionId,
                    timestampMillis = action.event.timestampMillis,
                    tagId = action.event.tagId,
                    note = state.value.eventNote
                )
                viewModelScope.launch { appDao.upsertEvent(event) }
                _state.update { it.copy(
                    eventNote = "",
                    isEditingEventNote = false) }
            }
            is AppAction.AppendEvent -> {
                val event = Event(
                    sessionId = action.sessionId,
                    timestampMillis = action.timestamp,
                    tagId = action.tagId
                )
                viewModelScope.launch { appDao.upsertEvent(event) }
            }
            is AppAction.UpdateEventNote -> {
                _state.update { it.copy(eventNote = action.noteText) }
            }
            is AppAction.EditEventNote -> {
                _state.update { it.copy(
                    eventNote = action.event.note,
                    isEditingEventNote = true
                ) }
            }
            is AppAction.HideDeleteEventDialog -> {
                _state.update { it.copy(showEventDeleteDialog = false) }
            }
            is AppAction.ShowDeleteEventDialog -> {
                _state.update { it.copy(showEventDeleteDialog = true) }
            }
            // Session
            AppAction.AddNewSessionClicked -> {
                _state.update { it.copy(
                    sessionColor = it.currentSession.color,
                    showSessionDialog = true,
                    isAddingNewSession = true
                ) }
            }
            is AppAction.AcceptAddingNewSessionClicked -> {
                val session = Session(
                    name = state.value.sessionName,
                    color = state.value.sessionColor,
                    lastAccessMillis = System.currentTimeMillis()
                )
                _state.update { it.copy(
                    showSessionDialog = false,
                    isAddingNewSession = false,
                    sessionColor = 0,
                    sessionName = ""
                ) }
                viewModelScope.launch { appDao.upsertSession(session) }
            }
            is AppAction.EditSessionClicked -> {
                _state.update { it.copy(
                    showSessionDialog = true,
                    isEditingSession = true,
                    lastAccessMillis = System.currentTimeMillis(),
                    currentSession = action.session,
                    sessionColor = action.session.color,
                    sessionName = action.session.name
                ) }
            }
            is AppAction.AcceptSessionEditionClicked -> {
                val session = Session(
                    id = state.value.currentSession.id,
                    startTimeMillis = action.session.startTimeMillis,
                    endTimeMillis = action.session.endTimeMillis,
                    name = state.value.sessionName,
                    color = state.value.sessionColor,
                    lastAccessMillis = System.currentTimeMillis()
                )
//                Log.d(TAG, "editing session, original id: ${state.value.currentSession.id}")
//                Log.d(TAG, "editing session, new id: ${session.id}")
                _state.update { it.copy(
                    showSessionDialog = false,
                    isEditingSession = false,
                    sessionColor = 0,
                    sessionName = ""
                ) }
                viewModelScope.launch { appDao.upsertSession(session) }
            }
            is AppAction.DismissSessionDialog -> {
                _state.update { it.copy(
                    showSessionDialog = false,
                    isEditingSession = false,
                    isAddingNewSession = false,
                    sessionColor = 0,
                    sessionName = ""
                ) }
            }
            is AppAction.UpdateSessionName -> {
                _state.update { it.copy(sessionName = action.name) }
            }
            is AppAction.UpdateSessionColor -> {
                _state.update { it.copy(sessionColor = action.color) }
            }
            is AppAction.DeleteSessionClicked -> {
                _state.update {
                    it.copy(
                        showSessionDeleteDialog = true,
                        currentSession = action.session
                    )
                }
            }
            AppAction.AcceptDeleteSessionClicked -> {
                viewModelScope.launch {
                    appDao.deleteSession(state.value.currentSession)
                    appDao.deleteEventsForSession(state.value.currentSession.id)
                }
                _state.update {
                    it.copy(
                        showSessionDeleteDialog = false,
                        currentSession = Session()
                    )
                }
            }
            is AppAction.DismissDeleteSessionDialog -> {
                _state.update { it.copy(showSessionDeleteDialog = false) }
            }
            is AppAction.SessionItemClicked -> {
                //TODO
            }
            // Tag
            is AppAction.DeleteTag -> {
                viewModelScope.launch { appDao.deleteTag(action.tag) }
            }
            is AppAction.TagEdited -> {
                val tag = Tag(
                    id = action.tag.id,
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
                _state.update { it.copy(tagLabel = action.label) }
            }
            is AppAction.EditTag -> {
                _state.update { it.copy(
                    tagLabel = action.tag.label,
                    tagColor = action.tag.color,
                    tagCategory = action.tag.category,
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
                _state.update { it.copy(tagCategory = action.category) }
            }
            is AppAction.UpdateTagColor -> {
                _state.update { it.copy(tagColor = action.color) }
            }
            is AppAction.UpsertTag -> {
                viewModelScope.launch { appDao.upsertTag(action.tag) }
            }
            // UsedTag
            is AppAction.UpsertUsedTag -> {
                viewModelScope.launch { appDao.upsertUsedTag(action.usedTag) }
            }
            is AppAction.DeleteUsedTag -> {
                viewModelScope.launch { appDao.deleteUsedTag(action.usedTag) }
            }
            is AppAction.ShowUsedTagDeleteDialog -> {
                _state.update { it.copy(showDeleteUsedTagDialog = true) }
            }
            is AppAction.HideUsedTagDeleteDialog -> {
                _state.update { it.copy(showDeleteUsedTagDialog = false) }
            }


        }
    }

    companion object {
        private const val TAG = "AppViewModel"
    }
}
package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.TagAction
import com.deslomator.tagtimer.dao.TagDao
import com.deslomator.tagtimer.model.Tag
import com.deslomator.tagtimer.state.TagState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TagViewModel(private val dao: TagDao): ViewModel() {

    private val _state = MutableStateFlow(TagState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TagState())

    fun onAction(action: TagAction) {
        when(action) {
            is TagAction.DeleteTag -> {
                viewModelScope.launch { dao.deleteTag(action.tag) }
            }
            is TagAction.TagEdited -> {
                val tag = Tag(
                    id = action.tag.id,
                    category = _state.value.category,
                    label = _state.value.label,
                    color = _state.value.color
                )
                viewModelScope.launch { dao.upsertTag(tag) }
                _state.update { it.copy(
                    category = "",
                    label = "",
                    color = 0,
                    isEditingTag = false) }
            }
            is TagAction.AddTag -> {
                _state.update { it.copy(isEditingTag = true) }
            }
            is TagAction.TagAdded -> {
                val tag = Tag(
                    label = _state.value.label,
                    color = _state.value.color,
                    category = _state.value.category
                )
                _state.update { it.copy(
                    isEditingTag = false,
                    color = 0,
                    category = "",
                    label = ""
                ) }
                viewModelScope.launch { dao.upsertTag(tag) }
            }
            is TagAction.UpdateLabel -> {
                _state.update { it.copy(label = action.label) }
            }
            is TagAction.EditTag -> {
                _state.update { it.copy(
                    editingIndex = action.editingIndex,
                    label = action.tag.label,
                    color = action.tag.color,
                    category = action.tag.category,
                    isEditingTag = true
                ) }
            }
            is TagAction.HideDeleteDialog -> {
                _state.update { it.copy(showDeleteDialog = false) }
            }
            is TagAction.ShowDeleteDialog -> {
                _state.update { it.copy(showDeleteDialog = true) }
            }

            is TagAction.UpdateCategory -> {
                _state.update { it.copy(category = action.category) }
            }
            is TagAction.UpdateColor -> {
                _state.update { it.copy(color = action.color) }
            }
            is TagAction.UpsertTag -> {
                viewModelScope.launch { dao.upsertTag(action.tag) }
            }
        }
    }
}
package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.TagsTabAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Tag
import com.deslomator.tagtimer.state.TagsTabState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagsTabViewModel @Inject constructor(
    private val appDao: AppDao,
): ViewModel() {

    private val _state = MutableStateFlow(TagsTabState())
    private val _tags = appDao.getActiveTags()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val state = combine(_state, _tags) { state, tags ->
        state.copy(
            tags = tags,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TagsTabState())

    fun onAction(action: TagsTabAction) {
        when(action) {
            is TagsTabAction.AddNewTagClicked -> {
                _state.update { it.copy(
                    tagColor = it.currentTag.color,
                    showTagDialog = true,
                    isAddingNewTag = true
                ) }
            }
            is TagsTabAction.AcceptAddNewTagClicked -> {
                val tag = Tag(
                    category = state.value.tagCategory,
                    label = state.value.tagLabel,
                    color = state.value.tagColor,
                )
                _state.update { it.copy(
                    showTagDialog = false,
                    isAddingNewTag = false,
                    tagCategory = "",
                    tagColor = 0,
                    tagLabel = ""
                ) }
                viewModelScope.launch { appDao.upsertTag(tag) }
            }
            is TagsTabAction.EditTagClicked -> {
                _state.update { it.copy(
                    showTagDialog = true,
                    isEditingTag = true,
                    currentTag = action.tag,
                    tagCategory = action.tag.category,
                    tagColor = action.tag.color,
                    tagLabel = action.tag.label
                ) }
            }
            is TagsTabAction.AcceptTagEditionClicked -> {
                val tag = Tag(
                    id = state.value.currentTag.id,
                    category = state.value.tagCategory,
                    label = state.value.tagLabel,
                    color = state.value.tagColor,
                )
                _state.update { it.copy(
                    showTagDialog = false,
                    isEditingTag = false,
                    tagLabel = "",
                    tagCategory = "",
                    tagColor = 0,
                    currentTag = Tag()
                ) }
                viewModelScope.launch { appDao.upsertTag(tag) }
            }
            is TagsTabAction.DismissTagDialog -> {
                _state.update { it.copy(
                    showTagDialog = false,
                    isEditingTag = false,
                    isAddingNewTag = false,
                    tagCategory = "",
                    tagLabel = "",
                    tagColor = 0,
                ) }
            }
            is TagsTabAction.UpdateTagCategory -> {
                _state.update { it.copy(tagCategory = action.category) }
            }
            is TagsTabAction.UpdateTagLabel -> {
                _state.update { it.copy(tagLabel = action.label) }
            }
            is TagsTabAction.UpdateTagColor -> {
                _state.update { it.copy(tagColor = action.color) }
            }
            is TagsTabAction.TrashTagSwiped -> {
                viewModelScope.launch {
                    val trashed = action.tag.copy(inTrash = true)
                    appDao.upsertTag(trashed)
//                    Log.d(TAG, "TagsScreenAction.TrashTagSwiped $trashed")
                }
            }
        }
    }

    companion object {
        private const val TAG = "TagsScreenViewModel"
    }
}
package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.LabelPreselectionAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Preselected
import com.deslomator.tagtimer.state.LabelPreselectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LabelPreselectionViewModel @Inject constructor(
    private val appDao: AppDao, savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _sessionId = MutableStateFlow(0)
    private val _state = MutableStateFlow(LabelPreselectionState())

    private val _tags = appDao.getActiveTags()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _preSelectedTags = _sessionId
        .flatMapLatest {
            appDao.getPreSelectedTagsForSession(_sessionId.value)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _persons = appDao.getActivePersons()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _preSelectedPersons = _sessionId
        .flatMapLatest {
            appDao.getPreSelectedPersonsForSession(_sessionId.value)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _places = appDao.getActivePlaces()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _preSelectedPlaces = _sessionId
        .flatMapLatest {
            appDao.getPreSelectedPlacesForSession(_sessionId.value)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = combine(
        _state, _preSelectedTags, _tags, _preSelectedPersons, _persons,
        _preSelectedPlaces, _places
    ) { state, preSelectedTags, tags, preSelectedPersons, persons,
        preSelectedPlaces, places ->
        state.copy(
            preSelectedTags = preSelectedTags,
            tags = tags,
            preSelectedPersons = preSelectedPersons,
            persons = persons,
            preSelectedPlaces = preSelectedPlaces,
            places = places,
        )
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), LabelPreselectionState()
    )

    fun onAction(action: LabelPreselectionAction) {
        when(action) {
            is LabelPreselectionAction.SelectTagCheckedChange -> {
                if (action.checked) {
                    val pst = Preselected.Tag (
                        sessionId = state.value.currentSession.id,
                        labelId = action.tagId
                    )
                    viewModelScope.launch {
                        delay(UPSERT_DELAY_MS)
                        appDao.upsertPreSelectedTag(pst)
                    }
                } else {
                    viewModelScope.launch {
                        delay(UPSERT_DELAY_MS)
                        appDao.deletePreSelectedTagForSession(
                            state.value.currentSession.id,
                            action.tagId
                        )
                    }
                }
            }
            is LabelPreselectionAction.SelectPersonCheckedChange -> {
                if (action.checked) {
                    val pst = Preselected.Person (
                        sessionId = state.value.currentSession.id,
                        labelId = action.personId
                    )
                    viewModelScope.launch {
                        delay(UPSERT_DELAY_MS)
                        appDao.upsertPreSelectedPerson(pst)
                    }
                } else {
                    viewModelScope.launch {
                        delay(UPSERT_DELAY_MS)
                        appDao.deletePreSelectedPersonForSession(
                            state.value.currentSession.id,
                            action.personId
                        )
                    }
                }
            }
            is LabelPreselectionAction.SelectPlaceCheckedChange -> {
                if (action.checked) {
                    val psPlace = Preselected.Place (
                        sessionId = state.value.currentSession.id,
                        labelId = action.placeId
                    )
                    viewModelScope.launch {
                        delay(UPSERT_DELAY_MS)
                        appDao.upsertPreSelectedPlace(psPlace)
                    }
                } else {
                    viewModelScope.launch {
                        delay(UPSERT_DELAY_MS)
                        appDao.deletePreSelectedPlaceForSession(
                            state.value.currentSession.id,
                            action.placeId
                        )
                    }
                }
            }
        }
    }

    private inline fun <T1, T2, T3, T4, T5, T6, T7, R> combine(
        flow1: Flow<T1>,
        flow2: Flow<T2>,
        flow3: Flow<T3>,
        flow4: Flow<T4>,
        flow5: Flow<T5>,
        flow6: Flow<T6>,
        flow7: Flow<T7>,
        crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7) -> R
    ): Flow<R> {
        return combine(flow1, flow2, flow3, flow4, flow5, flow6, flow7) { args: Array<*> ->
            @Suppress("UNCHECKED_CAST")
            transform(
                args[0] as T1,
                args[1] as T2,
                args[2] as T3,
                args[3] as T4,
                args[4] as T5,
                args[5] as T6,
                args[6] as T7,
            )
        }
    }

    init {
        val sessionId = savedStateHandle.get<Int>("sessionId") ?: 0
        _sessionId.update { sessionId }
        viewModelScope.launch {
            _state.update {
                it.copy(currentSession = appDao.getSession(sessionId))
            }
        }
    }

    companion object {
        private const val UPSERT_DELAY_MS = 300L
        private const val TAG = "LabelPreselectionViewModel"
    }
}
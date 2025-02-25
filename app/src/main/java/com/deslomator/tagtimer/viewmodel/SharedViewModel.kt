package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.SharedAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Preference
import com.deslomator.tagtimer.model.type.PrefKey
import com.deslomator.tagtimer.model.type.LabelSort
import com.deslomator.tagtimer.state.SharedState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val appDao: AppDao,
) : ViewModel() {

    private val _prefs = appDao.getPreferences()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(SharedState())
    val state = combine(_state, _prefs) { state, prefs ->
        state.copy(
            tagSort = prefs.firstOrNull { it.key == PrefKey.TAG_SORT.name }?.getLabelSort() ?: LabelSort.COLOR,
            personSort = prefs.firstOrNull { it.key == PrefKey.PERSON_SORT.name }?.getLabelSort() ?: LabelSort.NAME,
            placeSort = prefs.firstOrNull { it.key == PrefKey.PLACE_SORT.name }?.getLabelSort() ?: LabelSort.NAME,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SharedState()
    )

    fun onAction(action: SharedAction) {
        when(action) {
            is SharedAction.TagSortClicked -> {
                val pref = Preference(
                    key = PrefKey.TAG_SORT.name,
                    value = action.tagSort.name
                )
                viewModelScope.launch { appDao.upsertPreference(pref) }
            }
            is SharedAction.PersonSortClicked -> {
                val pref = Preference(
                    key = PrefKey.PERSON_SORT.name,
                    value = action.personSort.name
                )
                viewModelScope.launch { appDao.upsertPreference(pref) }
            }
            is SharedAction.PlaceSortClicked -> {
                val pref = Preference(
                    key = PrefKey.PLACE_SORT.name,
                    value = action.placeSort.name
                )
                viewModelScope.launch { appDao.upsertPreference(pref) }
            }
        }
    }

    companion object {
        private const val TAG = "SharedViewModel"
    }
}
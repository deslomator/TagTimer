package com.deslomator.tagtimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deslomator.tagtimer.action.SharedAction
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Preference
import com.deslomator.tagtimer.model.type.PrefKey
import com.deslomator.tagtimer.model.type.Sort
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
            tagSort = prefs.firstOrNull { it.sKey == PrefKey.TAG_SORT.name }?.getSort() ?: Sort.COLOR,
            personSort = prefs.firstOrNull { it.sKey == PrefKey.PERSON_SORT.name }?.getSort() ?: Sort.NAME,
            placeSort = prefs.firstOrNull { it.sKey == PrefKey.PLACE_SORT.name }?.getSort() ?: Sort.NAME,
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
                    value = action.tagSort.name,
                    sKey = PrefKey.TAG_SORT.name
                )
                viewModelScope.launch { appDao.upsertPreference(pref) }
            }
            is SharedAction.PersonSortClicked -> {
                val pref = Preference(
                    value = action.personSort.name,
                    sKey = PrefKey.PERSON_SORT.name
                )
                viewModelScope.launch { appDao.upsertPreference(pref) }
            }
            is SharedAction.PlaceSortClicked -> {
                val pref = Preference(
                    value = action.placeSort.name,
                    sKey = PrefKey.PLACE_SORT.name
                )
                viewModelScope.launch { appDao.upsertPreference(pref) }
            }
        }
    }

    companion object {
        private const val TAG = "SharedViewModel"
    }
}
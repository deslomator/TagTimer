package com.deslomator.tagtimer.model.type

import androidx.annotation.Keep
import com.deslomator.tagtimer.model.Preference

/**
 * Types of Preference
 */
@Keep
enum class PrefKey {
    PERSON_SORT,
    PLACE_SORT,
    TAG_SORT,
    SESSION_SORT,
    SHOW_ENABLED_LABELS,
    SHOW_ARCHIVED_LABELS,
    SHOW_TRASHED_LABELS,
    SHOW_ENABLED_SESSIONS,
    SHOW_ARCHIVED_SESSIONS,
    SHOW_TRASHED_SESSIONS,
}

data class PreferenceProvider(
    val preferences: List<Preference> = emptyList(),
) {

    private fun labelSort(key: PrefKey, default: LabelSort): LabelSort {
        val sort = preferences.firstOrNull { it.prefKey == key }?.getLabelSort()
        return sort ?: default
    }

    fun tagSort() = labelSort(PrefKey.TAG_SORT, LabelSort.COLOR)
    fun personSort() = labelSort(PrefKey.PERSON_SORT, LabelSort.NAME)
    fun placeSort() = labelSort(PrefKey.PLACE_SORT, LabelSort.NAME)

    fun sessionSort(): SessionSort {
        val sort = preferences.firstOrNull { it.prefKey == PrefKey.SESSION_SORT }?.getSessionSort()
        return sort ?: SessionSort.NAME
    }

    private fun showItem(key: PrefKey, default: Boolean): Boolean {
        val result = preferences.firstOrNull { it.prefKey == key }?.getBoolean()
        return result ?: default
    }

    fun showEnabledLabels() = showItem(PrefKey.SHOW_ENABLED_LABELS, true)
    fun showArchivedLabels() = showItem(PrefKey.SHOW_ARCHIVED_LABELS, false)
    fun showTrashedLabels() = showItem(PrefKey.SHOW_TRASHED_LABELS, false)

    fun showEnabledSessions() = showItem(PrefKey.SHOW_ENABLED_SESSIONS, true)
    fun showArchivedSessions() = showItem(PrefKey.SHOW_ARCHIVED_SESSIONS, false)
    fun showTrashedSessions() = showItem(PrefKey.SHOW_TRASHED_SESSIONS, false)
}
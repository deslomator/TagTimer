package com.deslomator.tagtimer.navigation.screen

import com.deslomator.tagtimer.R
import kotlinx.serialization.Serializable

@Serializable
enum class BottomScreens(
    val stringId: Int,
    val iconId: Int,
) {
    ACTIVE(R.string.session, R.drawable.document_and_ray),
    LABELS(R.string.labels, R.drawable.tag),
    FILTER(R.string.filter_events, R.drawable.filter),
    TRASH(R.string.trash, R.drawable.delete);

    fun getRoute(sessionId: Long?): Any {
        return when (this) {
            ACTIVE -> ActiveSessionScreen(sessionId = sessionId)
            LABELS -> LabelSelectionScreen(sessionId = sessionId)
            FILTER -> EventFilterScreen(sessionId = sessionId)
            TRASH -> TrashTabScreen(sessionId = sessionId)
        }
    }
}

@Serializable
object SessionsTabScreen

@Serializable
data class TrashTabScreen(
    val sessionId: Long?
)

@Serializable
object BackupScreen

@Serializable
data class ActiveSessionScreen(
    val sessionId: Long?
)

@Serializable
data class LabelSelectionScreen(
    val sessionId: Long?
)

@Serializable
data class EventFilterScreen(
    val sessionId: Long?
)
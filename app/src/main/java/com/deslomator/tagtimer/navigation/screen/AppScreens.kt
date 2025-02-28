package com.deslomator.tagtimer.navigation.screen

import com.deslomator.tagtimer.R
import kotlinx.serialization.Serializable

@Serializable
enum class MyBottomScreens(
    val stringId: Int,
    val iconId: Int,
    val route: Any
) {
    SESSIONS(R.string.sessions, R.drawable.document_and_ray, SessionsTabScreen),
    LABELS(R.string.tags, R.drawable.tag, LabelsTabScreen),
    TRASH(R.string.trash, R.drawable.delete, TrashTabScreen)
}

@Serializable
object SessionsTabScreen

@Serializable
object LabelsTabScreen

@Serializable
object TrashTabScreen


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

@Serializable
data class EventTrashScreen(
    val sessionId: Long?
)
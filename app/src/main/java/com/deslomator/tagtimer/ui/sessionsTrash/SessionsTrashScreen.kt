package com.deslomator.tagtimer.ui.sessionsTrash

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.deslomator.tagtimer.action.AppAction
import com.deslomator.tagtimer.action.SessionsTrashAction
import com.deslomator.tagtimer.state.SessionsTrashState
import com.deslomator.tagtimer.ui.Screen

@Composable
fun SessionsTrashScreen(
    onAppAction: (AppAction) -> Unit,
    state: SessionsTrashState,
    onAction: (SessionsTrashAction) -> Unit
) {
    Scaffold(
        topBar = { SessionsTrashTopBar(
            onBackClick = { onAppAction(AppAction.activateScreen(Screen.SESSIONS)) },
        ) },
        content = { paddingValues ->
            SessionsTrashContent(
                paddingValues = paddingValues,
                state = state,
                onAction = onAction
            )
        },
    )
}

private const val TAG = "SessionsTrashScreen"
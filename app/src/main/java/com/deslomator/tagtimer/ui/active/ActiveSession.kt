package com.deslomator.tagtimer.ui.active

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.state.ActiveSessionState

@Composable
fun ActiveSession(
    sessionId: Int,
    navHostController: NavHostController,
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    /*if (sessionsScreenState.showSnackbar) {
        LaunchedEffect(key1 = snackbarHostState) {
//            Log.d(TAG, "launching snackbar")
            snackbarHostState.showSnackbar(
                message = context.getString(R.string.session_sent_to_trash),
                duration = SnackbarDuration.Short
            )
            onSessionsAction(SessionsScreenAction.HideSnackbar)
        }
    }*/
    LaunchedEffect(Unit) {
//        Log.d(TAG, "LaunchedEffect")
        onAction(ActiveSessionAction.UpdateSessionId(sessionId))
    }
    Scaffold(
        topBar = {
            ActiveSessionTopBar(
                state = state,
                onBackClicked = {
                    if (state.showTagsDialog) {
                        onAction(ActiveSessionAction.DismissTagDialog)
                    } else {
                        onAction(ActiveSessionAction.StopSession)
                        navHostController.navigateUp()
                    }
                },
                onPlayPauseClick = { onAction(ActiveSessionAction.PlayPauseClicked) },
                onAddTagClick = { onAction(ActiveSessionAction.SelectTagsClicked) },
            )
        },
        bottomBar = { },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            ActiveSessionContent(
                state = state,
                onAction = onAction,
            )
        }
    }
}

private const val TAG = "ActiveSession"
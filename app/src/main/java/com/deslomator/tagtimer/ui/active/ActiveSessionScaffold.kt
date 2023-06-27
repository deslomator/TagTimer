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
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.state.ActiveSessionState

@Composable
fun ActiveSessionScaffold(
    sessionId: Int,
    navHostController: NavHostController,
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
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
                    } else if (state.showEventTrash) {
                        onAction(ActiveSessionAction.DismissEventTrashDialog)
                    } else {
                        onAction(ActiveSessionAction.StopSession)
                        navHostController.navigateUp()
                    }
                },
                onShareSessionClick = { onAction(ActiveSessionAction.ExportSessionClicked) },
                onEditSessionClick = { onAction(ActiveSessionAction.EditSessionClicked) },
                onAddTagClick = { onAction(ActiveSessionAction.SelectTagsClicked) },
                onEventTrashClick = { onAction(ActiveSessionAction.EventTrashClicked) },
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
                snackbarHostState = snackbarHostState
            )
        }
    }
}

private const val TAG = "ActiveSession"
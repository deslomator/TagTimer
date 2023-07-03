package com.deslomator.tagtimer.ui.active.trash

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.action.EventTrashAction
import com.deslomator.tagtimer.state.EventTrashState

@Composable
fun EventTrashScaffold(
    navController: NavHostController,
    state: EventTrashState,
    onAction: (EventTrashAction) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = {
            EventTrashTopBar(
                title = state.currentSession.name,
                onBackClicked = {
                    if (state.showEventInTrashDialog) {
                        onAction(EventTrashAction.DismissEventInTrashDialog)
                    } else {
                        navController.navigateUp()
                    }
                },
            )
        },
    ) { paddingValues ->
        EventTrashContent(
            paddingValues = paddingValues,
            state = state,
            onAction = onAction,
            snackbarHostState = snackbarHostState
        )
    }
}

private const val TAG = "TrashScaffold"
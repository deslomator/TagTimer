package com.deslomator.tagtimer.ui.active.trash

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.state.ActiveSessionState

@Composable
fun TrashScaffold(
    navController: NavHostController,
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = {
            EventTrashTopBar(
                state = state,
                onBackClicked = {
                    if (state.showEventInTrashDialog) {
                        onAction(ActiveSessionAction.DismissEventInTrashDialog)
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
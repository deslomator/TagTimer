package com.deslomator.tagtimer.ui.active.selection

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.state.ActiveSessionState

@Composable
fun SelectionScaffold(
    navController: NavHostController,
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit,
) {
    Scaffold(
        topBar = {
            SelectionTopBar(
                state = state,
                onBackClicked = {
                    navController.navigateUp()
                },
            )
        },
    ) { paddingValues ->
        LabelSelectionContent(
            paddingValues = paddingValues,
            state = state,
            onAction = onAction,
        )
    }
}

private const val TAG = "ActiveSession"
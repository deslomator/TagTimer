package com.deslomator.tagtimer.ui.active.selection

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.action.LabelPreselectionAction
import com.deslomator.tagtimer.state.LabelPreselectionState

@Composable
fun LabelSelectionScaffold(
    navController: NavHostController,
    state: LabelPreselectionState,
    onAction: (LabelPreselectionAction) -> Unit,
) {
    Scaffold(
        topBar = {
            LabelSelectionTopBar(
                title = state.currentSession.name,
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
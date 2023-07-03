package com.deslomator.tagtimer.ui.main.labels

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.deslomator.tagtimer.action.LabelsTabAction
import com.deslomator.tagtimer.state.LabelsTabState

@Composable
fun LabelsScaffold(
    state: LabelsTabState,
    onAction: (LabelsTabAction) -> Unit,
    bottomBar: @Composable () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = {
            LabelsTopBar(
                onNewTagClick = { onAction(LabelsTabAction.AddNewTagClicked) },
                onNewPersonClick = { onAction(LabelsTabAction.AddNewPersonClicked) },
                onNewPlaceClick = { onAction(LabelsTabAction.AddNewPlaceClicked) },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = { paddingValues ->
            LabelsTabContent(
                paddingValues = paddingValues,
                state = state,
                onAction = onAction,
                snackbarHostState = snackbarHostState
            )
        },
        bottomBar = bottomBar
    )
}


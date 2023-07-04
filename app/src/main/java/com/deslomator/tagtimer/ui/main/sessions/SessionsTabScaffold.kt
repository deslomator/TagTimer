package com.deslomator.tagtimer.ui.main.sessions

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.action.SessionsTabAction
import com.deslomator.tagtimer.state.SessionsTabState

@Composable
fun SessionsTabScaffold(
    navController: NavHostController,
    state: SessionsTabState,
    onAction: (SessionsTabAction) -> Unit,
    bottomBar: @Composable () -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = {
            SessionsTabTopBar(
                onNewSessionClick = { onAction(SessionsTabAction.AddNewSessionClicked) },
                onPopulateDbClick = { onAction(SessionsTabAction.PopulateDbClicked) },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = { paddingValues ->
            SessionsTabContent(
                paddingValues = paddingValues,
                outerNavHostController = navController,
                state = state,
                onAction = onAction,
                snackbarHostState = snackbarHostState
            )
        },
        bottomBar = bottomBar
    )
}


package com.deslomator.tagtimer.ui.main.sessions

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.action.SessionsTabAction
import com.deslomator.tagtimer.navigation.screen.BackupScreen
import com.deslomator.tagtimer.state.SessionsScreenState

@Composable
fun SessionsScreenScaffold(
    state: SessionsScreenState,
    onAction: (SessionsTabAction) -> Unit,
    navController: NavHostController,
) {

    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = {
            SessionsScreenTopBar(
                onNewSessionClick = { onAction(SessionsTabAction.AddNewSessionClicked) },
                onPopulateDbClick = { onAction(SessionsTabAction.PopulateDbClicked) },
                onBackupClick = { navController.navigate(BackupScreen) },
                onSessionSortClick = { onAction(SessionsTabAction.SessionSortClicked(it)) },
                currentSort = state.preferenceProvider.sessionSort(),
                showEnabled = state.preferenceProvider.showEnabledSessions(),
                onShowEnabledClick = { onAction(SessionsTabAction.ShowEnabledClicked(it)) },
                showArchived = state.preferenceProvider.showArchivedSessions(),
                onShowArchivedClick = { onAction(SessionsTabAction.ShowArchivedClicked(it)) },
                showTrashed = state.preferenceProvider.showTrashedSessions(),
                onShowTrashedClick = { onAction(SessionsTabAction.ShowTrashedClicked(it)) },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = { paddingValues ->
            SessionsScreenContent(
                paddingValues = paddingValues,
                outerNavHostController = navController,
                state = state,
                onAction = onAction,
                snackbarHostState = snackbarHostState
            )
        },
    )
}


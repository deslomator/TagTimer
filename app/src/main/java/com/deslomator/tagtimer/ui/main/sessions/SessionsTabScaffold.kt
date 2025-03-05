package com.deslomator.tagtimer.ui.main.sessions

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.action.SessionsTabAction
import com.deslomator.tagtimer.navigation.screen.BackupScreen
import com.deslomator.tagtimer.navigation.screen.MyBottomScreens
import com.deslomator.tagtimer.state.SessionsTabState
import com.deslomator.tagtimer.ui.main.BottomNavigationBar

@Composable
fun SessionsTabScaffold(
    state: SessionsTabState,
    onAction: (SessionsTabAction) -> Unit,
    navController: NavHostController,
) {

    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = {
            SessionsTabTopBar(
                onNewSessionClick = { onAction(SessionsTabAction.AddNewSessionClicked) },
                onPopulateDbClick = { onAction(SessionsTabAction.PopulateDbClicked) },
                onBackupClick = { navController.navigate(BackupScreen) },
                onSessionSortClick = { onAction(SessionsTabAction.SessionSortClicked(it)) },
                currentSort = state.sessionSort
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
    )
}


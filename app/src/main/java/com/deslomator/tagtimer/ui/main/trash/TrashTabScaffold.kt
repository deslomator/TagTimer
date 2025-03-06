package com.deslomator.tagtimer.ui.main.trash

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.action.TrashTabAction
import com.deslomator.tagtimer.navigation.screen.MyTopScreens
import com.deslomator.tagtimer.state.TrashTabState
import com.deslomator.tagtimer.ui.active.TopNavigationBar

@Composable
fun TrashTabScaffold(
    state: TrashTabState,
    onAction: (TrashTabAction) -> Unit,
    navController: NavHostController
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = { paddingValues ->
            TrashTabContent(
                paddingValues = paddingValues,
                state = state,
                onAction = onAction,
                snackbarHostState = snackbarHostState
            )
        },
        bottomBar = {
            TopNavigationBar(
                sessionId = state.currentSession.id ?: 0L,
                navController = navController,
                selected = MyTopScreens.TRASH
            )
        },
    )
}


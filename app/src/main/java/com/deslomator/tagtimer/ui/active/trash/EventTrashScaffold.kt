package com.deslomator.tagtimer.ui.active.trash

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.action.EventTrashAction
import com.deslomator.tagtimer.navigation.screen.MyTopScreens
import com.deslomator.tagtimer.state.EventTrashState
import com.deslomator.tagtimer.ui.active.TopNavigationBar

@Composable
fun EventTrashScaffold(
    navController: NavHostController,
    state: EventTrashState,
    onAction: (EventTrashAction) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = { },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            TopNavigationBar(
                sessionId = state.currentSession.id ?: 0L,
                navController = navController,
                selected = MyTopScreens.TRASH
            )
        }
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
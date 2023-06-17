package com.deslomator.tagtimer.ui.main

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.SessionsScreenAction
import com.deslomator.tagtimer.action.SessionsTrashAction
import com.deslomator.tagtimer.state.SessionsScreenState
import com.deslomator.tagtimer.state.SessionsTrashState
import com.deslomator.tagtimer.ui.sessions.SessionsTopBar

@Composable
fun MainScreen(
    outerNavHostController: NavHostController,
    sessionsScreenState: SessionsScreenState,
    onSessionsAction: (SessionsScreenAction) -> Unit,
    sessionsTrashState: SessionsTrashState,
    onSessionsTrashAction: (SessionsTrashAction) -> Unit
) {
    val innerNavHostController: NavHostController = rememberNavController()
    val backStackEntry = innerNavHostController.currentBackStackEntryAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    if (sessionsScreenState.showSnackbar) {
        LaunchedEffect(key1 = snackbarHostState) {
//            Log.d(TAG, "launching snackbar")
            snackbarHostState.showSnackbar(
                message = context.getString(R.string.session_sent_to_trash),
                duration = SnackbarDuration.Short
            )
            onSessionsAction(SessionsScreenAction.HideSnackbar)
        }
    }
    if (sessionsTrashState.showSnackBar) {
        LaunchedEffect(key1 = snackbarHostState) {
//            Log.d(TAG, "launching snackbar")
            snackbarHostState.showSnackbar(
                message = context.getString(sessionsTrashState.snackbarMessage),
                duration = SnackbarDuration.Short
            )
            onSessionsTrashAction(SessionsTrashAction.HideSnackbar)
        }
    }
    Scaffold(
        topBar = {
            SessionsTopBar(
                backStackEntry = backStackEntry,
                onNewSessionClick = { onSessionsAction(SessionsScreenAction.AddNewSessionClicked) },
                onNewTagClick = { /*TODO*/ },
            )
        },
        bottomBar = {
            MainNavigationBar(
                backStackEntry,
                innerNavHostController)
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = { paddingValues ->
            BottomNavHost(
                paddingValues = paddingValues,
                outerNavHostController = outerNavHostController,
                innerNavHostController = innerNavHostController,
                sessionsScreenState = sessionsScreenState,
                onSessionsAction = onSessionsAction,
                sessionsTrashState = sessionsTrashState,
                onSessionsTrashAction = onSessionsTrashAction
            )
        }
    )
}


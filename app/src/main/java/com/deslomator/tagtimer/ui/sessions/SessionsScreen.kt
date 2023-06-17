package com.deslomator.tagtimer.ui.sessions

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.SessionsScreenAction
import com.deslomator.tagtimer.state.SessionsScreenState

@Composable
fun SessionsScreen(
    navController: NavController,
    state: SessionsScreenState,
    onAction: (SessionsScreenAction) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    if (state.showSnackbar) {
        LaunchedEffect(key1 = snackbarHostState) {
//            Log.d(TAG, "launching snackbar")
            snackbarHostState.showSnackbar(
                message = context.getString(R.string.session_sent_to_trash),
                duration = SnackbarDuration.Short
            )
            onAction(SessionsScreenAction.HideSnackbar)
        }
    }
    Scaffold(
        topBar = { SessionsTopBar(
            onNewSessionClick = { onAction(SessionsScreenAction.AddNewSessionClicked) },
            onManageTagsClick = { onAction(SessionsScreenAction.ManageTagsClicked) },
            onGoToTrashClick = {
                onAction(SessionsScreenAction.HideSnackbar)
                navController.navigate("sessionsTrash")
            },
        ) },
        content = { paddingValues ->
            SessionsScreenContent(
                paddingValues = paddingValues,
                state = state,
                onAction = onAction
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    )
}

private const val TAG = "SessionsScreen"
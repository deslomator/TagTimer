package com.deslomator.tagtimer.ui.active.session

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.ShareData
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.navigation.screen.BottomScreens
import com.deslomator.tagtimer.navigation.screen.SessionsTabScreen
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.ui.active.BottomNavigationBar
import com.deslomator.tagtimer.ui.active.dialog.EventEditionDialog
import com.deslomator.tagtimer.ui.active.dialog.TimeDialog

@Composable
fun ActiveSessionScaffold(
    sessionId: Long,
    navController: NavHostController,
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var fileName by remember { //TODO share file
        mutableStateOf("")
    }
    BackHandler(enabled = state.showTimeDialog || state.showEventEditionDialog) {
        onAction(ActiveSessionAction.DismissTimeDialog)
        onAction(ActiveSessionAction.DismissEventEditionDialog)
    }
    BackHandler(enabled = !state.showEventEditionDialog && !state.showTimeDialog) {
        onAction(ActiveSessionAction.ExitSession)
        navController.navigate(SessionsTabScreen) {
            popUpTo(SessionsTabScreen) {
                inclusive = false
            }
        }
    }
    if (state.shareData) {
        ShareData(
            context = context,
            fileName = fileName,
            data = state.dataToShare,
            onDataShared = { onAction(ActiveSessionAction.SessionShared) },
        )
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            bottomBar = {
                BottomNavigationBar(
                    sessionId = sessionId,
                    navController = navController,
                    selected = BottomScreens.ACTIVE
                )
            }
        ) { paddingValues ->
            ActiveSessionContent(
                paddingValues = paddingValues,
                state = state,
                onAction = onAction,
                snackbarHostState = snackbarHostState
            )
        }
        AnimatedVisibility(
            visible = state.showEventEditionDialog,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            EventEditionDialog(
                event4d = state.eventForDialog,
                onAccept = { onAction(ActiveSessionAction.AcceptEventEditionClicked(it)) },
                onDismiss = { onAction(ActiveSessionAction.DismissEventEditionDialog) },
            )
        }
        AnimatedVisibility(
            visible = state.showTimeDialog,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            TimeDialog(
                current = state.currentSession.durationMillis,
                onDismiss = { onAction(ActiveSessionAction.DismissTimeDialog) },
                onAccept = { onAction(ActiveSessionAction.AcceptTimeDialog(it)) }
            )
        }
    }
}

private const val TAG = "ActiveSessionScaffold"
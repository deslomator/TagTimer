package com.deslomator.tagtimer.ui.active.session

import androidx.activity.compose.BackHandler
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.action.SharedAction
import com.deslomator.tagtimer.navigation.screen.ActiveScreen
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.state.SharedState
import com.deslomator.tagtimer.ui.active.ExportSession

@Composable
fun ActiveSessionScaffold(
    navController: NavHostController,
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit,
    sharedState: SharedState,
    onSharedAction: (SharedAction) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var fileName by remember {
        mutableStateOf("")
    }
    BackHandler(enabled = state.showTimeDialog || state.showEventEditionDialog) {
        onAction(ActiveSessionAction.DismissTimeDialog)
        onAction(ActiveSessionAction.DismissEventEditionDialog)
    }
    BackHandler(enabled = !state.showEventEditionDialog && !state.showTimeDialog) {
        onAction(ActiveSessionAction.UpdateSession)
        onSharedAction(SharedAction.StopSession)
        navController.navigate("root") {
            popUpTo("root") {
                inclusive = false
            }
        }
    }
    if (state.exportData) {
        ExportSession(
            context = context,
            fileName = fileName,
            data = state.dataToExport,
            onSessionExported = { onAction(ActiveSessionAction.SessionExported) }
        )
    }
    Scaffold(
        topBar = {
            ActiveSessionTopBar(
                state = state,
                onBackClicked = {
                    if (state.showEventEditionDialog) {
                        onAction(ActiveSessionAction.DismissEventEditionDialog)
                    } else if (state.showTimeDialog) {
                        onAction(ActiveSessionAction.DismissTimeDialog)
                    } else {
                        onAction(ActiveSessionAction.UpdateSession)
                        onSharedAction(SharedAction.StopSession)
                        navController.navigate("root") {
                            popUpTo("root") {
                                inclusive = false
                            }
                        }
                    }
                },
                onShareSessionClick = {
                    fileName = state.currentSession.name
                    onAction(ActiveSessionAction.ExportSessionClicked)
                },
                onAddLabelClick = {
                    onAction(ActiveSessionAction.UpdateSession)
                    navController.navigate(
                        ActiveScreen.LabelSelection.routeWithArg(state.currentSession.id)
                    )
                },
                onEventTrashClick = {
                    onAction(ActiveSessionAction.UpdateSession)
                    navController.navigate(
                        ActiveScreen.EventTrash.routeWithArg(state.currentSession.id)
                        )
                },
                onFilterClick = {
                    onAction(ActiveSessionAction.UpdateSession)
                    navController.navigate(
                        ActiveScreen.EventFilter.routeWithArg(state.currentSession.id)
                    )
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->
        ActiveSessionContent(
            paddingValues = paddingValues,
            state = state,
            onAction = onAction,
            sharedState = sharedState,
            onSharedAction = onSharedAction,
            snackbarHostState = snackbarHostState
        )
    }
}

private const val TAG = "ActiveSessionScaffold"
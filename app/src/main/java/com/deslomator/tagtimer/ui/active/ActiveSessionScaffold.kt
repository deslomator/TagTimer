package com.deslomator.tagtimer.ui.active

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
import com.deslomator.tagtimer.navigation.screen.ActiveScreen
import com.deslomator.tagtimer.state.ActiveSessionState

@Composable
fun ActiveSessionScaffold(
    navController: NavHostController,
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var fileName by remember {
        mutableStateOf("")
    }
    if (state.exportData) {
        ExportSession(
            context = context,
            fileName = fileName,
            data = state.dataToExport,
            onAction
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
                        onAction(ActiveSessionAction.StopSession)
                        navController.navigate("root") {
                            popUpTo("active") {
                                inclusive = true
                            }
                        }
                    }
                },
                onShareSessionClick = {
                    fileName = state.currentSession.name
                    onAction(ActiveSessionAction.ExportSessionClicked)
                },
                onAddLabelClick = {
                    onAction(ActiveSessionAction.StopSession)
                    navController.navigate(
                        ActiveScreen.LabelSelection.routeWithArg(state.currentSession.id)
                    )
                },
                onEventTrashClick = {
                    onAction(ActiveSessionAction.StopSession)
                    navController.navigate(
                        ActiveScreen.EventTrash.routeWithArg(state.currentSession.id)
                        )
                },
                onFilterClick = {
                    onAction(ActiveSessionAction.StopSession)
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
            snackbarHostState = snackbarHostState
        )
    }
}

private const val TAG = "ActiveSessionScaffold"
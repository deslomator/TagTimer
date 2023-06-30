package com.deslomator.tagtimer.ui.active

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.navigation.ActiveNavHost
import com.deslomator.tagtimer.navigation.screen.ActiveNavigationScreen
import com.deslomator.tagtimer.state.ActiveSessionState

@Composable
fun ActiveSessionScaffold(
    sessionId: Int,
    outerNavHostController: NavHostController,
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit,
) {
    val innerNavHostController: NavHostController = rememberNavController()
    val backStackEntry = innerNavHostController.currentBackStackEntryAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var fileName by remember {
        mutableStateOf("")
    }
    val filteredEvents by remember(
        state.currentPlaceName,
        state.currentPersonName,
        state.currentLabelName,
        state.events
    ) {
        derivedStateOf {
            state.events
                .filter { event ->
                    (if (state.currentPlaceName.isEmpty()) true else event.place == state.currentPlaceName) &&
                            (if (state.currentPersonName.isEmpty()) true else event.person == state.currentPersonName) &&
                            (if (state.currentLabelName.isEmpty()) true else event.label.contains(state.currentLabelName))
                }
        }
    }
    val totalEvents by remember(filteredEvents) {
        derivedStateOf { filteredEvents.size }
    }
    LaunchedEffect(Unit) {
        onAction(ActiveSessionAction.UpdateSessionId(sessionId))
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
                backStackEntry = backStackEntry,
                state = state,
                onBackClicked = {
                    if (state.showTagsDialog) {
                        onAction(ActiveSessionAction.DismissTagDialog)
                    } else if (state.showEventTrash) {
                        onAction(ActiveSessionAction.DismissEventTrashDialog)
                    } else if (state.showEventEditionDialog) {
                        onAction(ActiveSessionAction.DismissEventEditionDialog)
                    } else if (state.showSessionEditionDialog) {
                        onAction(ActiveSessionAction.DismissSessionEditionDialog)
                    } else {
                        onAction(ActiveSessionAction.StopSession)
                        if (backStackEntry.value?.destination?.route == ActiveNavigationScreen.EventFilter.route) {
                            innerNavHostController.navigateUp()
                        } else {
                            outerNavHostController.navigateUp()
                        }
                    }
                },
                onShareSessionClick = {
                    fileName = state.currentSession.name
                    onAction(ActiveSessionAction.ExportSessionClicked)
                },
                onEditSessionClick = { onAction(ActiveSessionAction.EditSessionClicked) },
                onAddTagClick = { onAction(ActiveSessionAction.SelectTagsClicked) },
                onEventTrashClick = { onAction(ActiveSessionAction.EventTrashClicked) },
                onFilterClick = {
                    onAction(ActiveSessionAction.StopSession)
                    innerNavHostController.navigate(ActiveNavigationScreen.EventFilter.route) {
                        popUpTo(innerNavHostController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onShareFilteredEventsClick = {
                    fileName = listOf(
                        state.currentSession.name,
                        state.currentPersonName,
                        state.currentPlaceName,
                        state.currentLabelName,
                    )
                        .filter { it.isNotEmpty() }
                        .joinToString(separator = ",")
                    onAction(ActiveSessionAction.ExportFilteredEventsClicked(filteredEvents))
                },
                totalEvents = totalEvents
            )
        },
        bottomBar = { },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->
        ActiveNavHost(
            paddingValues = paddingValues,
            innerNavHostController = innerNavHostController,
            state = state,
            onAction = onAction,
            snackbarHostState = snackbarHostState,
            filteredEvents = filteredEvents
        )
    }
}

private const val TAG = "ActiveSession"
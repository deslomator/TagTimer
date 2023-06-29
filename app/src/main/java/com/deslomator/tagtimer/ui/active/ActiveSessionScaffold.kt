package com.deslomator.tagtimer.ui.active

import androidx.activity.compose.BackHandler
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
    LaunchedEffect(Unit) {
        onAction(ActiveSessionAction.UpdateSessionId(sessionId))
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
                onShareSessionClick = { onAction(ActiveSessionAction.ExportSessionClicked) },
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
            snackbarHostState = snackbarHostState
        )
    }
}

private const val TAG = "ActiveSession"
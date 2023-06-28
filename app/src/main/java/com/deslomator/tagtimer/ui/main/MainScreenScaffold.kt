package com.deslomator.tagtimer.ui.main

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.deslomator.tagtimer.action.SessionsTabAction
import com.deslomator.tagtimer.action.LabelsTabAction
import com.deslomator.tagtimer.action.TrashTabAction
import com.deslomator.tagtimer.state.SessionsTabState
import com.deslomator.tagtimer.state.LabelsTabState
import com.deslomator.tagtimer.state.TrashTabState

@Composable
fun MainScreenScaffold(
    outerNavHostController: NavHostController,
    sessionsTabState: SessionsTabState,
    onSessionsAction: (SessionsTabAction) -> Unit,
    labelsTabState: LabelsTabState,
    onTagsAction: (LabelsTabAction) -> Unit,
    trashTabState: TrashTabState,
    onSessionsTrashAction: (TrashTabAction) -> Unit
) {
    val innerNavHostController: NavHostController = rememberNavController()
    val backStackEntry = innerNavHostController.currentBackStackEntryAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = {
            MainTopBar(
                backStackEntry = backStackEntry,
                onNewSessionClick = { onSessionsAction(SessionsTabAction.AddNewSessionClicked) },
                onNewTagClick = { onTagsAction(LabelsTabAction.AddNewTagClicked) },
                onNewPersonClick = { onTagsAction(LabelsTabAction.AddNewPersonClicked) },
                onNewPlaceClick = { onTagsAction(LabelsTabAction.AddNewPlaceClicked) },
                trashState = trashTabState,
                onTrashTypeClick = { onSessionsTrashAction(TrashTabAction.TrashTypeClicked(it)) },
                labelsTabState = labelsTabState,
                onLabelTypeClick = { onTagsAction(LabelsTabAction.LabelTypeSelected(it)) },
            )
        },
        bottomBar = {
            MainNavigationBar(
                backStackEntry,
                innerNavHostController
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = { paddingValues ->
            BottomNavHost(
                paddingValues = paddingValues,
                outerNavHostController = outerNavHostController,
                innerNavHostController = innerNavHostController,
                sessionsTabState = sessionsTabState,
                onSessionsAction = onSessionsAction,
                labelsTabState = labelsTabState,
                onTagsAction = onTagsAction,
                trashTabState = trashTabState,
                onSessionsTrashAction = onSessionsTrashAction,
                snackbarHostState = snackbarHostState
            )
        }
    )
}


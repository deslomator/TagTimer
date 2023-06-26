package com.deslomator.tagtimer.ui.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.deslomator.tagtimer.action.SessionsTabAction
import com.deslomator.tagtimer.action.TrashTabAction
import com.deslomator.tagtimer.action.TagsTabAction
import com.deslomator.tagtimer.state.SessionsTabState
import com.deslomator.tagtimer.state.TrashTabState
import com.deslomator.tagtimer.state.TagsTabState
import com.deslomator.tagtimer.ui.main.sessions.SessionsTabContent
import com.deslomator.tagtimer.ui.main.trash.TrashTabContent
import com.deslomator.tagtimer.ui.main.tags.TagsTabContent

@Composable
fun BottomNavHost(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    outerNavHostController: NavHostController,
    innerNavHostController: NavHostController,
    sessionsTabState: SessionsTabState,
    onSessionsAction: (SessionsTabAction) -> Unit,
    tagsTabState: TagsTabState,
    onTagsAction: (TagsTabAction) -> Unit,
    trashTabState: TrashTabState,
    onSessionsTrashAction: (TrashTabAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    NavHost(
        modifier = modifier,
        navController = innerNavHostController,
        startDestination = BottomNavigationScreen.Sessions.route,
    ) {
        composable(BottomNavigationScreen.Sessions.route) {
            SessionsTabContent(
                paddingValues = paddingValues,
                outerNavHostController = outerNavHostController,
                state = sessionsTabState,
                onAction = onSessionsAction,
                snackbarHostState = snackbarHostState
            )
        }
        composable(BottomNavigationScreen.Tags.route) {
            TagsTabContent(
                paddingValues = paddingValues,
                state = tagsTabState,
                onAction = onTagsAction,
                snackbarHostState = snackbarHostState
            )
        }
        composable(BottomNavigationScreen.Trash.route) {
            TrashTabContent(
                paddingValues = paddingValues,
                state = trashTabState,
                onAction = onSessionsTrashAction,
                snackbarHostState = snackbarHostState
            )
        }
    }
}
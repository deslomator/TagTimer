package com.deslomator.tagtimer.ui.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.deslomator.tagtimer.action.SessionsScreenAction
import com.deslomator.tagtimer.action.TrashTabAction
import com.deslomator.tagtimer.action.TagsScreenAction
import com.deslomator.tagtimer.state.SessionsScreenState
import com.deslomator.tagtimer.state.TrashTabState
import com.deslomator.tagtimer.state.TagsScreenState
import com.deslomator.tagtimer.ui.main.sessions.SessionsScreenContent
import com.deslomator.tagtimer.ui.main.trash.TrashContent
import com.deslomator.tagtimer.ui.main.tags.TagsScreenContent

@Composable
fun BottomNavHost(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    outerNavHostController: NavHostController,
    innerNavHostController: NavHostController,
    sessionsScreenState: SessionsScreenState,
    onSessionsAction: (SessionsScreenAction) -> Unit,
    tagsScreenState: TagsScreenState,
    onTagsAction: (TagsScreenAction) -> Unit,
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
            SessionsScreenContent(
                paddingValues = paddingValues,
                outerNavHostController = outerNavHostController,
                state = sessionsScreenState,
                onAction = onSessionsAction,
                snackbarHostState = snackbarHostState
            )
        }
        composable(BottomNavigationScreen.Tags.route) {
            TagsScreenContent(
                paddingValues = paddingValues,
                state = tagsScreenState,
                onAction = onTagsAction,
                snackbarHostState = snackbarHostState
            )
        }
        composable(BottomNavigationScreen.Trash.route) {
            TrashContent(
                paddingValues = paddingValues,
                state = trashTabState,
                onAction = onSessionsTrashAction,
                snackbarHostState = snackbarHostState
            )
        }
    }
}
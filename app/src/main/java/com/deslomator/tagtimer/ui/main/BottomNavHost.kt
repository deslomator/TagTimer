package com.deslomator.tagtimer.ui.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.deslomator.tagtimer.action.SessionsScreenAction
import com.deslomator.tagtimer.action.SessionsTrashAction
import com.deslomator.tagtimer.action.TagsScreenAction
import com.deslomator.tagtimer.state.SessionsScreenState
import com.deslomator.tagtimer.state.SessionsTrashState
import com.deslomator.tagtimer.state.TagsScreenState
import com.deslomator.tagtimer.ui.sessions.SessionsScreenContent
import com.deslomator.tagtimer.ui.sessionsTrash.SessionsTrashContent
import com.deslomator.tagtimer.ui.tags.TagsScreenContent

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
    sessionsTrashState: SessionsTrashState,
    onSessionsTrashAction: (SessionsTrashAction) -> Unit,
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
                onAction = onSessionsAction
            )
        }
        composable(BottomNavigationScreen.Tags.route) {
            TagsScreenContent(
                paddingValues = paddingValues,
                state = tagsScreenState,
                onAction = onTagsAction
            )
        }
        composable(BottomNavigationScreen.Trash.route) {
            SessionsTrashContent(
                paddingValues = paddingValues,
                state = sessionsTrashState,
                onAction = onSessionsTrashAction
            )
        }
    }
}
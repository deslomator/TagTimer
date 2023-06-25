package com.deslomator.tagtimer.ui.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.deslomator.tagtimer.action.SessionsScreenAction
import com.deslomator.tagtimer.action.TrashTabAction
import com.deslomator.tagtimer.action.TagsScreenAction
import com.deslomator.tagtimer.state.SessionsScreenState
import com.deslomator.tagtimer.state.SessionsTrashState
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
    sessionsTrashState: SessionsTrashState,
    onSessionsTrashAction: (TrashTabAction) -> Unit,
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
            TrashContent(
                paddingValues = paddingValues,
                state = sessionsTrashState,
                onAction = onSessionsTrashAction
            )
        }
    }
}
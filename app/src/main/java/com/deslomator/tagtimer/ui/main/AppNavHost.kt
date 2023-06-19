package com.deslomator.tagtimer.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.deslomator.tagtimer.action.SessionsScreenAction
import com.deslomator.tagtimer.action.SessionsTrashAction
import com.deslomator.tagtimer.action.TagsScreenAction
import com.deslomator.tagtimer.state.SessionsScreenState
import com.deslomator.tagtimer.state.SessionsTrashState
import com.deslomator.tagtimer.state.TagsScreenState
import com.deslomator.tagtimer.ui.active.ActiveSession

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    sessionsScreenState: SessionsScreenState,
    onSessionsAction: (SessionsScreenAction) -> Unit,
    tagsScreenState: TagsScreenState,
    onTagsAction: (TagsScreenAction) -> Unit,
    sessionsTrashState: SessionsTrashState,
    onSessionsTrashAction: (SessionsTrashAction) -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = RootScreen.Main.route,
    ) {
        composable(
            route = RootScreen.Main.route,
        ) {
            MainScreen(
                outerNavHostController = navController,
                sessionsScreenState = sessionsScreenState,
                onSessionsAction = onSessionsAction,
                tagsScreenState = tagsScreenState,
                onTagsAction = onTagsAction,
                sessionsTrashState = sessionsTrashState,
                onSessionsTrashAction = onSessionsTrashAction
            )
        }
        composable(
            route = RootScreen.Active.route,
        ) {
            ActiveSession(
                navHostController = navController,
                sessionsScreenState = sessionsScreenState,
                onSessionsAction = onSessionsAction,
                onTagsAction = onTagsAction,
            )
        }
    }
}


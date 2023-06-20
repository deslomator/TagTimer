package com.deslomator.tagtimer.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.action.SessionsScreenAction
import com.deslomator.tagtimer.action.SessionsTrashAction
import com.deslomator.tagtimer.action.TagsScreenAction
import com.deslomator.tagtimer.state.ActiveSessionState
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
    activeSessionState: ActiveSessionState,
    onActiveSessionAction: (ActiveSessionAction) -> Unit,
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
            route = "${RootScreen.Active.route}/{sessionId}",
            arguments = listOf(navArgument("sessionId") { type = NavType.IntType })
        ) { backStackEntry ->
            ActiveSession(
                sessionId = backStackEntry.arguments?.getInt("sessionId") ?: 0,
                navHostController = navController,
                state = activeSessionState,
                onAction = onActiveSessionAction,
            )
        }
    }
}


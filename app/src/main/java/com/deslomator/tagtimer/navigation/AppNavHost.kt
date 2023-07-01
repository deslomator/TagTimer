package com.deslomator.tagtimer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.action.SessionsTabAction
import com.deslomator.tagtimer.action.TrashTabAction
import com.deslomator.tagtimer.action.LabelsTabAction
import com.deslomator.tagtimer.navigation.screen.RootScreen
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.state.SessionsTabState
import com.deslomator.tagtimer.state.TrashTabState
import com.deslomator.tagtimer.state.LabelsTabState
import com.deslomator.tagtimer.ui.active.ActiveSessionScaffold
import com.deslomator.tagtimer.ui.main.MainScreenScaffold

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    sessionsTabState: SessionsTabState,
    onSessionsAction: (SessionsTabAction) -> Unit,
    labelsTabState: LabelsTabState,
    onTagsAction: (LabelsTabAction) -> Unit,
    trashTabState: TrashTabState,
    onSessionsTrashAction: (TrashTabAction) -> Unit,
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
            MainScreenScaffold(
                outerNavHostController = navController,
                sessionsTabState = sessionsTabState,
                onSessionsAction = onSessionsAction,
                labelsTabState = labelsTabState,
                onTagsAction = onTagsAction,
                trashTabState = trashTabState,
                onSessionsTrashAction = onSessionsTrashAction
            )
        }
        composable(
            route = "${RootScreen.Active.route}/{sessionId}",
            arguments = listOf(navArgument("sessionId") { type = NavType.IntType })
        ) { backStackEntry ->
            ActiveSessionScaffold(
                sessionId = backStackEntry.arguments?.getInt("sessionId") ?: 0,
                outerNavHostController = navController,
                state = activeSessionState,
                onAction = onActiveSessionAction,
            )
        }
    }
}

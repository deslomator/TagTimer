package com.deslomator.tagtimer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.deslomator.tagtimer.navigation.screen.ActiveScreen
import com.deslomator.tagtimer.navigation.screen.RootScreen
import com.deslomator.tagtimer.ui.active.ActiveSessionScaffold
import com.deslomator.tagtimer.ui.active.filter.FilterScaffold
import com.deslomator.tagtimer.ui.active.selection.SelectionScaffold
import com.deslomator.tagtimer.ui.active.trash.TrashScaffold
import com.deslomator.tagtimer.viewmodel.ActiveSessionViewModel

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = "root",
    ) {
        navigation(
            route = "root",
            startDestination = RootScreen.Main.route
        ) {
            composable(
                route = RootScreen.Main.route,
            ) {
                BottomNavHost(
                    outerNavHostController = navController,
                )
            }
        }
        navigation(
            route = "active/{sessionId}",
            startDestination = ActiveScreen.ActiveSession.route,
            arguments = listOf(navArgument("sessionId") { type = NavType.IntType })
        ) {
            composable(
                route = ActiveScreen.ActiveSession.route
            ) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("active/{sessionId}")
                }
                val sessionId = parentEntry.arguments?.getInt("sessionId") ?: 0
                val viewModel = hiltViewModel<ActiveSessionViewModel>(backStackEntry)
                viewModel.updateId(sessionId)
                val activeState by viewModel.state.collectAsState()
                ActiveSessionScaffold(
                    navController = navController,
                    state = activeState,
                    onAction = viewModel::onAction,
                )
            }
            composable(
                route = ActiveScreen.EventFilter.route
            ) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("active/{sessionId}")
                }
                val sessionId = parentEntry.arguments?.getInt("sessionId") ?: 0
                val viewModel = hiltViewModel<ActiveSessionViewModel>(backStackEntry)
                viewModel.updateId(sessionId)
                val activeState by viewModel.state.collectAsState()
                FilterScaffold(
                    navController = navController,
                    state = activeState,
                    onAction = viewModel::onAction,
                )
            }
            composable(
                route = ActiveScreen.EventTrash.route
            ) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("active/{sessionId}")
                }
                val sessionId = parentEntry.arguments?.getInt("sessionId") ?: 0
                val viewModel = hiltViewModel<ActiveSessionViewModel>(backStackEntry)
                viewModel.updateId(sessionId)
                val activeState by viewModel.state.collectAsState()
                TrashScaffold(
                    navController = navController,
                    state = activeState,
                    onAction = viewModel::onAction,
                )
            }
            composable(
                route = ActiveScreen.LabelSelection.route
            ) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("active/{sessionId}")
                }
                val sessionId = parentEntry.arguments?.getInt("sessionId") ?: 0
                val viewModel = hiltViewModel<ActiveSessionViewModel>(backStackEntry)
                viewModel.updateId(sessionId)
                val activeState by viewModel.state.collectAsState()
                SelectionScaffold(
                    navController = navController,
                    state = activeState,
                    onAction = viewModel::onAction,
                )
            }
        }
    }
}


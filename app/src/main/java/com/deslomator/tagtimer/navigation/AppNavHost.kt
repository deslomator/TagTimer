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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.deslomator.tagtimer.navigation.screen.ActiveScreen
import com.deslomator.tagtimer.navigation.screen.BottomNavigationScreen
import com.deslomator.tagtimer.navigation.screen.RootScreen
import com.deslomator.tagtimer.ui.active.session.ActiveSessionScaffold
import com.deslomator.tagtimer.ui.active.filter.EventFilterScaffold
import com.deslomator.tagtimer.ui.active.selection.SelectionScaffold
import com.deslomator.tagtimer.ui.active.trash.EventTrashScaffold
import com.deslomator.tagtimer.ui.main.MainNavigationBar
import com.deslomator.tagtimer.ui.main.labels.LabelsScaffold
import com.deslomator.tagtimer.ui.main.sessions.SessionsTabScaffold
import com.deslomator.tagtimer.ui.main.trash.TrashTabScaffold
import com.deslomator.tagtimer.viewmodel.ActiveSessionViewModel
import com.deslomator.tagtimer.viewmodel.EventFilterViewModel
import com.deslomator.tagtimer.viewmodel.EventTrashViewModel
import com.deslomator.tagtimer.viewmodel.LabelPreselectionViewModel
import com.deslomator.tagtimer.viewmodel.LabelsTabViewModel
import com.deslomator.tagtimer.viewmodel.SessionsTabViewModel
import com.deslomator.tagtimer.viewmodel.TrashTabViewModel

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
                val innerNavHostController = rememberNavController()
                val backStackEntry = innerNavHostController.currentBackStackEntryAsState()
                NavHost(
                    navController = innerNavHostController,
                    startDestination = BottomNavigationScreen.Sessions.route,
                ) {
                    composable(BottomNavigationScreen.Sessions.route) {
                        val viewModel = hiltViewModel<SessionsTabViewModel>(it)
                        val state by viewModel.state.collectAsState()
                        val onAction = viewModel::onAction
                        SessionsTabScaffold(
                            navController = navController,
                            state = state,
                            onAction = onAction,
                        ) {
                            MainNavigationBar(
                                backStackEntry = backStackEntry,
                                innerNavHostController = innerNavHostController
                            )
                        }
                    }
                    composable(BottomNavigationScreen.Labels.route) {
                        val viewModel = hiltViewModel<LabelsTabViewModel>(it)
                        val state by viewModel.state.collectAsState()
                        val onAction= viewModel::onAction
                        LabelsScaffold(
                            state = state,
                            onAction = onAction,
                        ) {
                            MainNavigationBar(
                                backStackEntry = backStackEntry,
                                innerNavHostController = innerNavHostController
                            )
                        }
                    }
                    composable(BottomNavigationScreen.Trash.route) {
                        val viewModel = hiltViewModel<TrashTabViewModel>(it)
                        val state by viewModel.state.collectAsState()
                        val onAction= viewModel::onAction
                        TrashTabScaffold(
                            state = state,
                            onAction = onAction,
                        ) {
                            MainNavigationBar(
                                backStackEntry = backStackEntry,
                                innerNavHostController = innerNavHostController
                            )
                        }
                    }
                }
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
                val state by viewModel.state.collectAsState()
                ActiveSessionScaffold(
                    navController = navController,
                    state = state,
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
                val viewModel = hiltViewModel<EventFilterViewModel>(backStackEntry)
                viewModel.updateId(sessionId)
                val state by viewModel.state.collectAsState()
                EventFilterScaffold(
                    navController = navController,
                    state = state,
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
                val viewModel = hiltViewModel<EventTrashViewModel>(backStackEntry)
                viewModel.updateId(sessionId)
                val state by viewModel.state.collectAsState()
                EventTrashScaffold(
                    navController = navController,
                    state = state,
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
                val viewModel = hiltViewModel<LabelPreselectionViewModel>(backStackEntry)
                viewModel.updateId(sessionId)
                val state by viewModel.state.collectAsState()
                SelectionScaffold(
                    navController = navController,
                    state = state,
                    onAction = viewModel::onAction,
                )
            }
        }
    }
}


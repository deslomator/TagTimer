package com.deslomator.tagtimer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
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
import com.deslomator.tagtimer.ui.active.filter.EventFilterScaffold
import com.deslomator.tagtimer.ui.active.selection.LabelSelectionScaffold
import com.deslomator.tagtimer.ui.active.session.ActiveSessionScaffold
import com.deslomator.tagtimer.ui.active.trash.EventTrashScaffold
import com.deslomator.tagtimer.ui.backup.BackupScaffold
import com.deslomator.tagtimer.ui.main.MainNavigationBar
import com.deslomator.tagtimer.ui.main.labels.LabelsScaffold
import com.deslomator.tagtimer.ui.main.sessions.SessionsTabScaffold
import com.deslomator.tagtimer.ui.main.trash.TrashTabScaffold
import com.deslomator.tagtimer.viewmodel.ActiveSessionViewModel
import com.deslomator.tagtimer.viewmodel.BackupViewModel
import com.deslomator.tagtimer.viewmodel.EventFilterViewModel
import com.deslomator.tagtimer.viewmodel.EventTrashViewModel
import com.deslomator.tagtimer.viewmodel.SharedViewModel
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
                        val state by viewModel.state.collectAsStateWithLifecycle()
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
                        val state by viewModel.state.collectAsStateWithLifecycle()
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
                        val state by viewModel.state.collectAsStateWithLifecycle()
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
            arguments = listOf(navArgument("sessionId") { type = NavType.LongType })
        ) {
            composable(
                route = ActiveScreen.ActiveSession.route,
                arguments = listOf(navArgument("sessionId") { type = NavType.LongType })
            ) { backStackEntry ->
                val sharedVm = backStackEntry.sharedViewModel<SharedViewModel>(navController)
                val sharedState by sharedVm.state.collectAsStateWithLifecycle()
                val viewModel = hiltViewModel<ActiveSessionViewModel>(backStackEntry)
                val state by viewModel.state.collectAsStateWithLifecycle()
                ActiveSessionScaffold(
                    navController = navController,
                    state = state,
                    onAction = viewModel::onAction,
                    sharedState = sharedState,
                )
            }
            composable(
                route = ActiveScreen.EventFilter.route,
                arguments = listOf(navArgument("sessionId") { type = NavType.LongType })
            ) { backStackEntry ->
                val sharedVm = backStackEntry.sharedViewModel<SharedViewModel>(navController)
                val sharedState by sharedVm.state.collectAsStateWithLifecycle()
                val viewModel = hiltViewModel<EventFilterViewModel>(backStackEntry)
                val state by viewModel.state.collectAsStateWithLifecycle()
                EventFilterScaffold(
                    navController = navController,
                    state = state,
                    sharedState = sharedState,
                    onAction = viewModel::onAction,
                )
            }
            composable(
                route = ActiveScreen.EventTrash.route,
                arguments = listOf(navArgument("sessionId") { type = NavType.LongType })
            ) { backStackEntry ->
                val viewModel = hiltViewModel<EventTrashViewModel>(backStackEntry)
                val state by viewModel.state.collectAsStateWithLifecycle()
                EventTrashScaffold(
                    navController = navController,
                    state = state,
                    onAction = viewModel::onAction,
                )
            }
            composable(
                route = ActiveScreen.LabelSelection.route,
                arguments = listOf(navArgument("sessionId") { type = NavType.LongType })
            ) { backStackEntry ->
                val sharedVm = backStackEntry.sharedViewModel<SharedViewModel>(navController)
                val sharedState by sharedVm.state.collectAsStateWithLifecycle()
                val viewModel = hiltViewModel<LabelPreselectionViewModel>(backStackEntry)
                val state by viewModel.state.collectAsStateWithLifecycle()
                LabelSelectionScaffold(
                    navController = navController,
                    state = state,
                    onAction = viewModel::onAction,
                    sharedState = sharedState,
                    onSharedAction = sharedVm::onAction,
                )
            }
        }
        navigation(
            route = "backup",
            startDestination = RootScreen.Backup.route
        ) {
            composable(
                route = RootScreen.Backup.route,
            ) {backStackEntry ->
                val viewModel = hiltViewModel<BackupViewModel>(backStackEntry)
                val state by viewModel.state.collectAsStateWithLifecycle()
                BackupScaffold(
                    navController = navController,
                    state = state,
                    onAction = viewModel::onAction,
                )
            }
        }
    }
}

@Composable
inline fun <reified T: ViewModel> NavBackStackEntry.sharedViewModel(
    navHost: NavHostController): T {
    val route = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navHost.getBackStackEntry(route)
    }
    return hiltViewModel(parentEntry)
}

private const val TAG = "AppNavHost"
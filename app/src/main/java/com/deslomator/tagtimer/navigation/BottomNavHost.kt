package com.deslomator.tagtimer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.deslomator.tagtimer.navigation.screen.BottomNavigationScreen
import com.deslomator.tagtimer.ui.main.MainNavigationBar
import com.deslomator.tagtimer.ui.main.labels.LabelsScaffold
import com.deslomator.tagtimer.ui.main.sessions.SessionsTabScaffold
import com.deslomator.tagtimer.ui.main.trash.TrashTabScaffold
import com.deslomator.tagtimer.viewmodel.LabelsTabViewModel
import com.deslomator.tagtimer.viewmodel.SessionsTabViewModel
import com.deslomator.tagtimer.viewmodel.TrashTabViewModel

@Composable
fun BottomNavHost(
    modifier: Modifier = Modifier,
    outerNavHostController: NavHostController,
) {
    val innerNavHostController = rememberNavController()
    val backStackEntry = innerNavHostController.currentBackStackEntryAsState()
    NavHost(
        modifier = modifier,
        navController = innerNavHostController,
        startDestination = BottomNavigationScreen.Sessions.route,
    ) {
        composable(BottomNavigationScreen.Sessions.route) {
            val sessionsTabViewModel = hiltViewModel<SessionsTabViewModel>(it)
            val state by sessionsTabViewModel.state.collectAsState()
            val onAction = sessionsTabViewModel::onAction
            SessionsTabScaffold(
                navController = outerNavHostController,
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
package com.deslomator.tagtimer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.deslomator.tagtimer.navigation.screen.ActiveSessionScreen
import com.deslomator.tagtimer.navigation.screen.BackupScreen
import com.deslomator.tagtimer.navigation.screen.EventFilterScreen
import com.deslomator.tagtimer.navigation.screen.EventTrashScreen
import com.deslomator.tagtimer.navigation.screen.LabelSelectionScreen
import com.deslomator.tagtimer.navigation.screen.LabelsTabScreen
import com.deslomator.tagtimer.navigation.screen.MyBottomScreens
import com.deslomator.tagtimer.navigation.screen.SessionsTabScreen
import com.deslomator.tagtimer.navigation.screen.TrashTabScreen
import com.deslomator.tagtimer.ui.active.filter.EventFilterScaffold
import com.deslomator.tagtimer.ui.active.selection.LabelSelectionScaffold
import com.deslomator.tagtimer.ui.active.session.ActiveSessionScaffold
import com.deslomator.tagtimer.ui.active.trash.EventTrashScaffold
import com.deslomator.tagtimer.ui.backup.BackupScaffold
import com.deslomator.tagtimer.ui.main.BottomNavigationBar
import com.deslomator.tagtimer.ui.main.labels.LabelsScaffold
import com.deslomator.tagtimer.ui.main.sessions.SessionsTabScaffold
import com.deslomator.tagtimer.ui.main.trash.TrashTabScaffold
import com.deslomator.tagtimer.viewmodel.ActiveSessionViewModel
import com.deslomator.tagtimer.viewmodel.BackupViewModel
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
        startDestination = SessionsTabScreen,
    ) {
        composable<SessionsTabScreen> { stackEntry ->
            val viewModel = hiltViewModel<SessionsTabViewModel>(stackEntry)
            val state by viewModel.state.collectAsStateWithLifecycle()
            val onAction = viewModel::onAction
            SessionsTabScaffold(
                navController = navController,
                state = state,
                onAction = onAction,
            ) {
                BottomNavigationBar(
                    navController = navController,
                    selected = MyBottomScreens.SESSIONS,
                )
            }
        }
        composable<LabelsTabScreen> { stackEntry ->
            val viewModel = hiltViewModel<LabelsTabViewModel>(stackEntry)
            val state by viewModel.state.collectAsStateWithLifecycle()
            val onAction = viewModel::onAction
            LabelsScaffold(
                state = state,
                onAction = onAction,
            ) {
                BottomNavigationBar(
                    navController = navController,
                    selected = MyBottomScreens.LABELS,
                )
            }
        }
        composable<TrashTabScreen> { stackEntry ->
            val viewModel = hiltViewModel<TrashTabViewModel>(stackEntry)
            val state by viewModel.state.collectAsStateWithLifecycle()
            val onAction = viewModel::onAction
            TrashTabScaffold(
                state = state,
                onAction = onAction,
            ) {
                BottomNavigationBar(
                    navController = navController,
                    selected = MyBottomScreens.TRASH,
                )
            }
        }
        composable<ActiveSessionScreen> { backStackEntry ->
            val viewModel = hiltViewModel<ActiveSessionViewModel>(backStackEntry)
            val state by viewModel.state.collectAsStateWithLifecycle()
            ActiveSessionScaffold(
                navController = navController,
                state = state,
                onAction = viewModel::onAction,
            )
        }
        composable<EventFilterScreen> { backStackEntry ->
            val viewModel = hiltViewModel<EventFilterViewModel>(backStackEntry)
            val state by viewModel.state.collectAsStateWithLifecycle()
            EventFilterScaffold(
                navController = navController,
                state = state,
                onAction = viewModel::onAction,
            )
        }
        composable<EventTrashScreen> { backStackEntry ->
            val viewModel = hiltViewModel<EventTrashViewModel>(backStackEntry)
            val state by viewModel.state.collectAsStateWithLifecycle()
            EventTrashScaffold(
                navController = navController,
                state = state,
                onAction = viewModel::onAction,
            )
        }
        composable<LabelSelectionScreen> { backStackEntry ->
            val viewModel = hiltViewModel<LabelPreselectionViewModel>(backStackEntry)
            val state by viewModel.state.collectAsStateWithLifecycle()
            LabelSelectionScaffold(
                navController = navController,
                state = state,
                onAction = viewModel::onAction,
            )
        }
        composable<BackupScreen> { backStackEntry ->
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

@Composable
inline fun <reified T : ViewModel> NavHostController.sharedViewModel(): T {
    val pbs = previousBackStackEntry
    return if (pbs != null) hiltViewModel<T>(pbs)
    else hiltViewModel()
}

const val TAG = "AppNavHost"
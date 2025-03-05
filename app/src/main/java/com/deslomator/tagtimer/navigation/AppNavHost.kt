package com.deslomator.tagtimer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.deslomator.tagtimer.navigation.screen.ActiveSessionScreen
import com.deslomator.tagtimer.navigation.screen.BackupScreen
import com.deslomator.tagtimer.navigation.screen.EventFilterScreen
import com.deslomator.tagtimer.navigation.screen.EventTrashScreen
import com.deslomator.tagtimer.navigation.screen.LabelSelectionScreen
import com.deslomator.tagtimer.navigation.screen.LabelsTabScreen
import com.deslomator.tagtimer.navigation.screen.SessionsTabScreen
import com.deslomator.tagtimer.navigation.screen.TrashTabScreen
import com.deslomator.tagtimer.ui.active.filter.EventFilterScaffold
import com.deslomator.tagtimer.ui.active.selection.LabelSelectionScaffold
import com.deslomator.tagtimer.ui.active.session.ActiveSessionScaffold
import com.deslomator.tagtimer.ui.active.trash.EventTrashScaffold
import com.deslomator.tagtimer.ui.backup.BackupScaffold
import com.deslomator.tagtimer.ui.main.labels.LabelsScaffold
import com.deslomator.tagtimer.ui.main.sessions.SessionsTabScaffold
import com.deslomator.tagtimer.ui.main.trash.TrashTabScaffold
import com.deslomator.tagtimer.viewmodel.ActiveSessionViewModel
import com.deslomator.tagtimer.viewmodel.BackupViewModel
import com.deslomator.tagtimer.viewmodel.EventFilterViewModel
import com.deslomator.tagtimer.viewmodel.EventTrashViewModel
import com.deslomator.tagtimer.viewmodel.LabelSelectionViewModel
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
        composable<SessionsTabScreen> { backStackEntry ->
            val viewModel = hiltViewModel<SessionsTabViewModel>(backStackEntry)
            val state by viewModel.state.collectAsStateWithLifecycle()
            SessionsTabScaffold(
                state = state,
                onAction = viewModel::onAction,
                navController = navController,
            )
        }
        composable<LabelsTabScreen> { backStackEntry ->
            val viewModel = hiltViewModel<LabelsTabViewModel>(backStackEntry)
            val state by viewModel.state.collectAsStateWithLifecycle()
            LabelsScaffold(
                state = state,
                onAction = viewModel::onAction,
                navController = navController,
            )
        }
        composable<TrashTabScreen> { backStackEntry ->
            val viewModel = hiltViewModel<TrashTabViewModel>(backStackEntry)
            val state by viewModel.state.collectAsStateWithLifecycle()
            TrashTabScaffold(
                state = state,
                onAction = viewModel::onAction,
                navController = navController,
            )
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
        composable<LabelSelectionScreen> { backStackEntry ->
            val viewModel = hiltViewModel<LabelSelectionViewModel>(backStackEntry)
            val state by viewModel.state.collectAsStateWithLifecycle()
            LabelSelectionScaffold(
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

// keep commented instead of deleting, it can be needed in the future
/*@Composable
inline fun <reified T : ViewModel> NavHostController.sharedViewModel(): T {
    val pbs = previousBackStackEntry
    return if (pbs != null) hiltViewModel<T>(pbs)
    else hiltViewModel()
}*/

const val TAG = "AppNavHost"
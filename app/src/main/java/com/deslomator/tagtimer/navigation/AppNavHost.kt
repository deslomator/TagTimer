package com.deslomator.tagtimer.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.deslomator.tagtimer.TagTimerApp
import com.deslomator.tagtimer.navigation.screen.ActiveSessionScreen
import com.deslomator.tagtimer.navigation.screen.BackupScreen
import com.deslomator.tagtimer.navigation.screen.EventFilterScreen
import com.deslomator.tagtimer.navigation.screen.LabelSelectionScreen
import com.deslomator.tagtimer.navigation.screen.SessionsTabScreen
import com.deslomator.tagtimer.navigation.screen.TrashTabScreen
import com.deslomator.tagtimer.ui.active.filter.EventFilterScaffold
import com.deslomator.tagtimer.ui.active.selection.LabelSelectionScaffold
import com.deslomator.tagtimer.ui.active.session.ActiveSessionScaffold
import com.deslomator.tagtimer.ui.backup.BackupScaffold
import com.deslomator.tagtimer.ui.main.sessions.SessionsTabScaffold
import com.deslomator.tagtimer.ui.active.trash.TrashScaffold
import com.deslomator.tagtimer.viewmodel.ActiveSessionViewModel
import com.deslomator.tagtimer.viewmodel.BackupViewModel
import com.deslomator.tagtimer.viewmodel.EventFilterViewModel
import com.deslomator.tagtimer.viewmodel.LabelSelectionViewModel
import com.deslomator.tagtimer.viewmodel.SessionsTabViewModel
import com.deslomator.tagtimer.viewmodel.TrashTabViewModel
import com.deslomator.tagtimer.viewmodel.viewModelFactory

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
        composable<SessionsTabScreen> {
            val viewModel = viewModel<SessionsTabViewModel>(
                factory = viewModelFactory {
                    SessionsTabViewModel(TagTimerApp.appModule.appDao)
                }
            )
            val state by viewModel.state.collectAsStateWithLifecycle()
            SessionsTabScaffold(
                state = state,
                onAction = viewModel::onAction,
                navController = navController,
            )
        }
        composable<TrashTabScreen> { backStackEntry ->
            val screen: TrashTabScreen = backStackEntry.toRoute()
            val viewModel = viewModel<TrashTabViewModel>(
                factory = viewModelFactory {
                    TrashTabViewModel(TagTimerApp.appModule.appDao, screen.sessionId)
                }
            )
            val state by viewModel.state.collectAsStateWithLifecycle()
            TrashScaffold(
                state = state,
                onAction = viewModel::onAction,
                navController = navController,
            )
        }
        composable<ActiveSessionScreen> { backStackEntry ->
            Log.d(TAG, "From NavHost. savedStateHandle keys: ${backStackEntry.savedStateHandle.keys()}")
            val screen: ActiveSessionScreen = backStackEntry.toRoute()
            val viewModel = viewModel<ActiveSessionViewModel>(
                factory = viewModelFactory {
                    ActiveSessionViewModel(TagTimerApp.appModule.appDao, screen.sessionId)
                }
            )
            val state by viewModel.state.collectAsStateWithLifecycle()
            ActiveSessionScaffold(
                navController = navController,
                state = state,
                onAction = viewModel::onAction,
            )
        }
        composable<LabelSelectionScreen> { backStackEntry ->
            val screen: LabelSelectionScreen = backStackEntry.toRoute()
            val viewModel = viewModel<LabelSelectionViewModel>(
                factory = viewModelFactory {
                    LabelSelectionViewModel(TagTimerApp.appModule.appDao, screen.sessionId)
                }
            )
            val state by viewModel.state.collectAsStateWithLifecycle()
            LabelSelectionScaffold(
                navController = navController,
                state = state,
                onAction = viewModel::onAction,
            )
        }
        composable<EventFilterScreen> { backStackEntry ->
            val screen: EventFilterScreen = backStackEntry.toRoute()
            val viewModel = viewModel<EventFilterViewModel>(
                factory = viewModelFactory {
                    EventFilterViewModel(TagTimerApp.appModule.appDao, screen.sessionId)
                }
            )
            val state by viewModel.state.collectAsStateWithLifecycle()
            EventFilterScaffold(
                navController = navController,
                state = state,
                onAction = viewModel::onAction,
            )
        }
        composable<BackupScreen> {
            val viewModel = viewModel<BackupViewModel>(
                factory = viewModelFactory {
                    BackupViewModel(TagTimerApp.appModule.appContext, TagTimerApp.appModule.appDao)
                }
            )
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
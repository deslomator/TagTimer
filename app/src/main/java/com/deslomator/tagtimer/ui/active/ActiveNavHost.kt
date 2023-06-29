package com.deslomator.tagtimer.ui.active

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.state.ActiveSessionState

@Composable
fun ActiveNavHost(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    innerNavHostController: NavHostController,
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    NavHost(
        modifier = modifier,
        navController = innerNavHostController,
        startDestination = ActiveNavigationScreen.ActiveSession.route,
    ) {
        composable(ActiveNavigationScreen.ActiveSession.route) {
            ActiveSessionContent(
                paddingValues = paddingValues,
                state = state,
                onAction = onAction,
                snackbarHostState = snackbarHostState
            )
        }
        composable(ActiveNavigationScreen.EventFilter.route) {
            ActiveSessionContent(
                paddingValues = paddingValues,
                state = state,
                onAction = onAction,
                snackbarHostState = snackbarHostState
            )
        }
    }
}
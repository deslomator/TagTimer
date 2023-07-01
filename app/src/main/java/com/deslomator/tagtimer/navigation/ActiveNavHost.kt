package com.deslomator.tagtimer.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.navigation.screen.ActiveNavigationScreen
import com.deslomator.tagtimer.ui.active.ActiveSessionContent
import com.deslomator.tagtimer.ui.active.filter.EventFilterContent
import com.deslomator.tagtimer.ui.active.selection.LabelSelectionContent
import com.deslomator.tagtimer.ui.active.trash.EventTrashContent

@Composable
fun ActiveNavHost(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    innerNavHostController: NavHostController,
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    filteredEvents: List<Event>
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
        composable(ActiveNavigationScreen.LabelSelection.route) {
            LabelSelectionContent(
                paddingValues = paddingValues,
                state = state,
                onAction = onAction,
            )
        }
        composable(ActiveNavigationScreen.EventFilter.route) {
            EventFilterContent(
                paddingValues = paddingValues,
                state = state,
                onAction = onAction,
                filteredEvents = filteredEvents
            )
        }
        composable(ActiveNavigationScreen.EventTrash.route) {
            EventTrashContent(
                paddingValues = paddingValues,
                state = state,
                onAction = onAction,
                snackbarHostState = snackbarHostState
            )
        }
    }
}
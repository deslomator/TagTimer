package com.deslomator.tagtimer.ui.active.filter

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.action.EventFilterAction
import com.deslomator.tagtimer.state.EventFilterState
import com.deslomator.tagtimer.state.SharedState
import com.deslomator.tagtimer.ui.active.ExportSession

@Composable
fun EventFilterScaffold(
    navController: NavHostController,
    state: EventFilterState,
    sharedState: SharedState,
    onAction: (EventFilterAction) -> Unit,
) {
    val context = LocalContext.current
    if (state.exportEvents) {
        val fileName = listOf(
            state.currentSession.name,
            state.query,
        )
            .filter { it.isNotEmpty() }
            .joinToString(separator = ",")
        ExportSession(
            context = context,
            fileName = fileName,
            data = state.dataToExport,
            onSessionExported = { onAction(EventFilterAction.EventsExported) }
        )
    }
    LaunchedEffect(sharedState.tagSort) {
        onAction(EventFilterAction.SetTagSort(sharedState.tagSort))
    }
    LaunchedEffect(sharedState.personSort) {
        onAction(EventFilterAction.SetPersonSort(sharedState.personSort))
    }
    LaunchedEffect(sharedState.placeSort) {
        onAction(EventFilterAction.SetPlaceSort(sharedState.placeSort))
    }
    Scaffold(
        topBar = {
            EventFilterTopBar(
                title = state.currentSession.name,
                onBackClicked = {
                    navController.navigateUp()
                },
                onShareFilteredEventsClick = {
                    onAction(EventFilterAction.ExportFilteredEventsClicked(state.events))
                },
                totalEvents = state.events.size
            )
        },
    ) { paddingValues ->
        EventFilterContent(
            paddingValues = paddingValues,
            state = state,
            onAction = onAction,
            filteredEvents = state.events
        )
    }
}

private const val TAG = "FilterScaffold"
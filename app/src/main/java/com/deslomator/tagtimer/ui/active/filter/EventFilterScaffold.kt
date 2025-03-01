package com.deslomator.tagtimer.ui.active.filter

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.ShareData
import com.deslomator.tagtimer.action.EventFilterAction
import com.deslomator.tagtimer.state.EventFilterState

@Composable
fun EventFilterScaffold(
    navController: NavHostController,
    state: EventFilterState,
    onAction: (EventFilterAction) -> Unit,
) {
    val context = LocalContext.current
    if (state.exportEvents) {
        // TODO check recomposition
        val fileName = listOf(
            state.currentSession.name,
            state.query,
        )
            .filter { it.isNotEmpty() }
            .joinToString(separator = ",")
        ShareData(
            context = context,
            fileName = fileName,
            data = state.dataToExport,
            onDataShared = { onAction(EventFilterAction.EventsExported) }
        )
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
            filteredEvents = state.eventsForDisplay
        )
    }
}

private const val TAG = "FilterScaffold"
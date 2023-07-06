package com.deslomator.tagtimer.ui.active.filter

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var fileName by remember {
        mutableStateOf("")
    }
    val filteredEvents by remember(
        state.currentPlaceName,
        state.currentPersonName,
        state.currentLabelName,
        state.events
    ) {
        derivedStateOf {
            state.events
                .filter { event ->
                    (if (state.currentPlaceName.isEmpty()) true else event.place == state.currentPlaceName) &&
                            (if (state.currentPersonName.isEmpty()) true else event.person == state.currentPersonName) &&
                            (if (state.currentLabelName.isEmpty()) true else event.tag.contains(state.currentLabelName))
                }
        }
    }
    val totalEvents by remember(filteredEvents) {
        derivedStateOf { filteredEvents.size }
    }
    if (state.exportEvents) {
        ExportSession(
            context = context,
            fileName = fileName,
            data = state.dataToExport,
            onSessionExported = { onAction(EventFilterAction.EventsExported) }
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
                    fileName = listOf(
                        state.currentSession.name,
                        state.currentPersonName,
                        state.currentPlaceName,
                        state.currentLabelName,
                    )
                        .filter { it.isNotEmpty() }
                        .joinToString(separator = ",")
                    onAction(EventFilterAction.ExportFilteredEventsClicked(filteredEvents))
                },
                totalEvents = totalEvents
            )
        },
    ) { paddingValues ->
        EventFilterContent(
            paddingValues = paddingValues,
            state = state,
            sharedState = sharedState,
            onAction = onAction,
            filteredEvents = filteredEvents
        )
    }
}

private const val TAG = "FilterScaffold"
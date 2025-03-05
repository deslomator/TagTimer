package com.deslomator.tagtimer.ui.active.filter

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.ShareData
import com.deslomator.tagtimer.action.EventFilterAction
import com.deslomator.tagtimer.navigation.screen.MyTopScreens
import com.deslomator.tagtimer.state.EventFilterState
import com.deslomator.tagtimer.ui.active.TopNavigationBar

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
            Column {
                TopNavigationBar(
                    sessionId = state.currentSession.id ?: 0L,
                    navController = navController,
                    selected = MyTopScreens.FILTER
                )
                EventFilterTopBar(
                    title = state.currentSession.name,
                    onBackClicked = {
                        navController.navigateUp()
                    },
                    onShareFilteredEventsClick = {
                        onAction(EventFilterAction.ExportFilteredEventsClicked(state.filteredEvents))
                    },
                    totalEvents = state.filteredEvents.size
                )
            }
        },
    ) { paddingValues ->
        EventFilterContent(
            paddingValues = paddingValues,
            state = state,
            onAction = onAction,
            filteredEvents = state.filteredEvents
        )
    }
}

private const val TAG = "FilterScaffold"
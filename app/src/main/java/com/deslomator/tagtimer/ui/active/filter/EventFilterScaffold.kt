package com.deslomator.tagtimer.ui.active.filter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.ShareData
import com.deslomator.tagtimer.action.EventFilterAction
import com.deslomator.tagtimer.navigation.screen.BottomScreens
import com.deslomator.tagtimer.state.EventFilterState
import com.deslomator.tagtimer.ui.active.BottomNavigationBar
import com.deslomator.tagtimer.ui.active.dialog.EventEditionDialog

@Composable
fun EventFilterScaffold(
    sessionId: Long,
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
    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Scaffold(
            topBar = {
                EventFilterTopBar(
                    onShareFilteredEventsClick = {
                        onAction(EventFilterAction.ExportFilteredEventsClicked(state.filteredEvents))
                    },
                    totalEvents = state.filteredEvents.size
                )
            },
            bottomBar = {
                BottomNavigationBar(
                    sessionId = sessionId,
                    navController = navController,
                    selected = BottomScreens.FILTER
                )
            }
        ) { paddingValues ->
            EventFilterContent(
                paddingValues = paddingValues,
                state = state,
                onAction = onAction,
                filteredEvents = state.filteredEvents
            )
        }
        AnimatedVisibility(
            visible = state.showEventEditionDialog,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            EventEditionDialog(
                event4d = state.eventForDialog,
                onAccept = { onAction(EventFilterAction.AcceptEventEditionClicked(it)) },
                onDismiss = { onAction(EventFilterAction.DismissEventEditionDialog) },
            )
        }
    }
}

private const val TAG = "FilterScaffold"
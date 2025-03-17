package com.deslomator.tagtimer.ui.active.trash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.action.TrashTabAction
import com.deslomator.tagtimer.navigation.screen.BottomScreens
import com.deslomator.tagtimer.state.TrashTabState
import com.deslomator.tagtimer.ui.active.BottomNavigationBar
import com.deslomator.tagtimer.ui.active.dialog.EventEditionDialog

@Composable
fun TrashScaffold(
    sessionId: Long,
    state: TrashTabState,
    onAction: (TrashTabAction) -> Unit,
    navController: NavHostController
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            content = { paddingValues ->
                TrashContent(
                    paddingValues = paddingValues,
                    state = state,
                    onAction = onAction,
                    snackbarHostState = snackbarHostState
                )
            },
            bottomBar = {
                BottomNavigationBar(
                    sessionId = sessionId,
                    navController = navController,
                    selected = BottomScreens.TRASH
                )
            },
        )
        AnimatedVisibility(
            visible = state.showEventInTrashDialog,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            EventEditionDialog(
                event4d = state.eventForDialog,
                onAccept = { onAction(TrashTabAction.DismissEventInTrashDialog) },
                onDismiss = { onAction(TrashTabAction.DismissEventInTrashDialog) },
                enabled = false
            )
        }
    }
}


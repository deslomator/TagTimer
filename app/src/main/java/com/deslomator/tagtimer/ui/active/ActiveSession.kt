package com.deslomator.tagtimer.ui.active

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.action.SessionsScreenAction
import com.deslomator.tagtimer.action.TagsScreenAction
import com.deslomator.tagtimer.state.SessionsScreenState

@Composable
fun ActiveSession(
    navHostController: NavHostController,
    sessionsScreenState: SessionsScreenState,
    onSessionsAction: (SessionsScreenAction) -> Unit,
    onTagsAction: (TagsScreenAction) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    /*if (sessionsScreenState.showSnackbar) {
        LaunchedEffect(key1 = snackbarHostState) {
//            Log.d(TAG, "launching snackbar")
            snackbarHostState.showSnackbar(
                message = context.getString(R.string.session_sent_to_trash),
                duration = SnackbarDuration.Short
            )
            onSessionsAction(SessionsScreenAction.HideSnackbar)
        }
    }*/
    Scaffold(
        topBar = {
            ActiveSessionTopBar(
                onBackClicked = { navHostController.navigateUp() },
                onNewSessionClick = { onSessionsAction(SessionsScreenAction.AddNewSessionClicked) },
                onNewTagClick = { onTagsAction(TagsScreenAction.AddNewTagClicked) },
            )
        },
        bottomBar = { },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            ActiveSessionContent(
                state = sessionsScreenState,
                onAction = onSessionsAction,
            )
        }
    }
}


package com.deslomator.tagtimer.ui.sessionsTrash

import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.SessionsScreenAction
import com.deslomator.tagtimer.state.SessionsScreenState

@Composable
fun SessionsTrashScreen(
    state: SessionsScreenState,
    onAction: (SessionsScreenAction) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    if (state.showSessionDeleteSnackbar) {
        LaunchedEffect(key1 = snackbarHostState) {
            Log.d(TAG, "launching snackbar")
            val result = snackbarHostState.showSnackbar(
                message = context.getString(R.string.session_sent_to_trash),
                duration = SnackbarDuration.Short
            )
            when (result) {
                SnackbarResult.Dismissed -> {}
                SnackbarResult.ActionPerformed -> {}
            }
            Log.d(TAG, "hiding snackbar")
            onAction(SessionsScreenAction.HideSessionDeletedSnackbar)
        }
    }
    Scaffold(
        topBar = { SessionsTrashTopBar(
            onBackClick = { onAction(SessionsScreenAction.ManageTagsClicked) },
        ) },
        content = { paddingValues ->
            SessionsTrashContent(
                paddingValues = paddingValues,
                state = state,
                onAction = onAction
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        )
}

private const val TAG = "SessionsScreen"
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
import com.deslomator.tagtimer.action.AppAction
import com.deslomator.tagtimer.action.SessionsTrashAction
import com.deslomator.tagtimer.state.SessionsTrashState
import com.deslomator.tagtimer.ui.Screen

@Composable
fun SessionsTrashScreen(
    onAppAction: (AppAction) -> Unit,
    state: SessionsTrashState,
    onAction: (SessionsTrashAction) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    if (state.showSnackBar) {
        LaunchedEffect(key1 = snackbarHostState) {
//            Log.d(TAG, "launching trash snackbar")
            val result = snackbarHostState.showSnackbar(
                message = context.getString(state.snackbarMessage),
                duration = SnackbarDuration.Short
            )
//            Log.d(TAG, "trash snackbar waiting result")
            /*when (result) {
                SnackbarResult.Dismissed -> {}
                SnackbarResult.ActionPerformed -> {}
            }*/
//            Log.d(TAG, "Hiding trash snackbar")
            onAction(SessionsTrashAction.HideSnackbar)
        }
    }
    Scaffold(
        topBar = { SessionsTrashTopBar(
            onBackClick = {
                onAction(SessionsTrashAction.HideSnackbar)
                onAppAction(AppAction.activateScreen(Screen.SESSIONS))
            },
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

private const val TAG = "SessionsTrashScreen"
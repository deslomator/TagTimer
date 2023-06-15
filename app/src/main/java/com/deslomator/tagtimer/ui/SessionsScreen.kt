package com.deslomator.tagtimer.ui

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DismissDirection.StartToEnd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.AppAction
import com.deslomator.tagtimer.state.AppState
import com.deslomator.tagtimer.ui.theme.Pink80

@Composable
fun SessionsScreen(
    state: AppState,
    onAction: (AppAction) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    if (state.showSessionDeleteSnackbar) {
        LaunchedEffect(key1 = snackbarHostState) {
            Log.d(TAG, "before snackbar result")
            val result = (snackbarHostState.showSnackbar(
                message = context.getString(R.string.session_deleted),
                actionLabel = context.getString(R.string.undo),
                duration = SnackbarDuration.Short
            ))
            Log.d(TAG, "waiting snackbar result")
            when (result){
                SnackbarResult.ActionPerformed -> onAction(AppAction.SnackbarUndoDeleteSessionClicked)
                SnackbarResult.Dismissed -> onAction(AppAction.SnackbarUndoDeleteSessionIgnored)
            }
            onAction(AppAction.HideSessionDeleteSnackbar)
            Log.d(TAG, "after snackbar result")
        }
    }
    Scaffold(
        topBar = { TopBar(
            onNewSessionClick = { onAction(AppAction.AddNewSessionClicked) },
            onManageTagsClick = { onAction(AppAction.ManageTagsClicked) },
        ) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = { paddingValues ->
            MainScreenContent(
                paddingValues = paddingValues,
                state = state,
                onAction = onAction
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onNewSessionClick: () -> Unit,
    onManageTagsClick: () -> Unit,
) {
    TopAppBar(
        title = { Text(stringResource(id = R.string.app_name)) },
        actions = {
            IconButton(
                onClick = onNewSessionClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_playlist_add_24),
                    contentDescription = stringResource(id = R.string.new_session)
                )
            }
            IconButton(
                onClick = onManageTagsClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_label_24),
                    contentDescription = stringResource(id = R.string.manage_tags)
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContent(
    paddingValues: PaddingValues,
    state: AppState,
    onAction: (AppAction) -> Unit,
) {
    if (state.showSessionDialog) {
        SessionDialog(
            state = state,
            onAction = onAction,
            session = state.currentSession
        )
    }
    /*if (state.showSessionDeleteDialog) {
        ConfirmationDialog(
            title = stringResource(id = R.string.delete_session),
            onAccept = { onAction(AppAction.AcceptDeleteSessionClicked) },
            onCancel = { onAction(AppAction.DismissDeleteSessionDialog) }
        )
    }*/
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = state.sessions,
                key = { it.id }
            ) { session ->
                SwipeableListItem(
                    dismissDirection = StartToEnd,
                    onDismiss = { onAction(AppAction.DeleteSessionSwiped(session)) },
                    dismissColor = Pink80
                ) { dismissState ->
                    SessionItem(
                        session = session,
                        onItemClick = { onAction(AppAction.SessionItemClicked(session)) },
                        onEditClick = { onAction(AppAction.EditSessionClicked(session)) },
//                                onDeleteClick = { onAction(AppAction.DeleteSessionSwiped(session)) },
                        shadowElevation = animateDpAsState(
                            if (dismissState.dismissDirection != null) 20.dp else 10.dp
                        ).value
                    )
                }
            }
        }
    }
}

private const val TAG = "SessionsScreen"
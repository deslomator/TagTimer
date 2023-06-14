package com.deslomator.tagtimer.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DismissDirection.StartToEnd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.AppAction
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.state.AppState
import com.deslomator.tagtimer.ui.theme.SoftGreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionsScreen(
    state: AppState,
    onAction: (AppAction) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onAction(AppAction.AddNewSessionClicked) }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.new_session)
                )
            }
        }
    ) { paddingValues ->
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
                items(state.sessions, { session: Session -> session.id }) { session ->
                    SwipeableListItem(
                        dismissDirection = StartToEnd,
                        onDismiss = { onAction(AppAction.DeleteSessionSwiped(session)) },
                        dismissColor = SoftGreen
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
}

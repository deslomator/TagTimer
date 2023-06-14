package com.deslomator.tagtimer.ui

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DismissDirection.EndToStart
import androidx.compose.material3.DismissDirection.StartToEnd
import androidx.compose.material3.DismissValue.Default
import androidx.compose.material3.DismissValue.DismissedToEnd
import androidx.compose.material3.DismissValue.DismissedToStart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.AppAction
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.state.AppState
import com.deslomator.tagtimer.ui.theme.Pink80


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
        if (state.showSessionDeleteDialog) {
            ConfirmationDialog(
                title = stringResource(id = R.string.delete_session),
                onAccept = { onAction(AppAction.AcceptDeleteSessionClicked) },
                onCancel = { onAction(AppAction.DismissDeleteSessionDialog) }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.sessions, { session: Session -> session.id }) { session ->

                    val dismissState = rememberDismissState(initialValue = Default)
                    if (dismissState.isDismissed(EndToStart)) {
                        onAction(AppAction.DeleteSessionClicked(session))
                    }

                    SwipeToDismiss(
                        state = dismissState,
                        modifier = Modifier.padding(vertical = 4.dp),
                        directions = setOf(EndToStart),
                        background = {
                            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
                            val color by animateColorAsState(
                                when (dismissState.targetValue) {
                                    Default -> Color.LightGray
                                    DismissedToEnd -> Color.Green
                                    DismissedToStart -> Pink80
                                }
                            )
                            val alignment = when (direction) {
                                StartToEnd -> Alignment.CenterStart
                                EndToStart -> Alignment.CenterEnd
                            }
                            val icon = when (direction) {
                                StartToEnd -> Icons.Default.Done
                                EndToStart -> Icons.Default.Delete
                            }
                            val scale by animateFloatAsState(
                                if (dismissState.targetValue == Default) 0.75f else 1f
                            )

                            Box(
                                Modifier.fillMaxSize().background(color)
                                    .padding(horizontal = 20.dp),
                                contentAlignment = alignment
                            ) {
                                Icon(
                                    icon,
                                    contentDescription = "Localized description",
                                    modifier = Modifier.scale(scale)
                                )
                            }
                        },
                        dismissContent = {
                            Log.d("Sessions Screen", "Loading item")
                            SessionItem(
                                session = session,
                                onItemClick = { onAction(AppAction.SessionItemClicked(session)) },
                                onEditClick = { onAction(AppAction.EditSessionClicked(session)) },
                                onDeleteClick = { onAction(AppAction.DeleteSessionClicked(session)) },
                                shadowElevation = animateDpAsState(
                                    if (dismissState.dismissDirection != null) 22.dp else 16.dp
                                ).value
                            )
                        }
                    )
                }
            }
        }
    }
}

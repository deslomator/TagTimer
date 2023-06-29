package com.deslomator.tagtimer.ui.active

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.toElapsedTime
import kotlinx.coroutines.delay

@Composable
fun ActiveSessionContent(
    paddingValues: PaddingValues,
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    BackHandler(enabled = state.showTagsDialog) {
        onAction(ActiveSessionAction.DismissTagDialog)
    }
    BackHandler(enabled = state.showEventTrash) {
        onAction(ActiveSessionAction.DismissEventTrashDialog)
    }
    BackHandler(enabled = state.showEventEditionDialog) {
        onAction(ActiveSessionAction.DismissEventEditionDialog)
    }
    BackHandler(enabled = state.showSessionEditionDialog) {
        onAction(ActiveSessionAction.DismissSessionEditionDialog)
    }
    LaunchedEffect(state.currentSession) {
        onAction(ActiveSessionAction.SetCursor(state.currentSession.durationMillis))
    }
    LaunchedEffect(state.isRunning) {
        if (state.isRunning) {
            while (true) {
                delay(1000)
                onAction(ActiveSessionAction.IncreaseCursor(1000))
            }
        }
    }
    if (state.exportSession) {
        ExportSession(context, state, onAction)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        Column {
            EventList(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.4f),
                state = state,
                onAction = onAction,
                snackbarHostState = snackbarHostState
            )
            Row(
                modifier = Modifier
                    .background(Color.LightGray)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { onAction(ActiveSessionAction.TimeClicked) }
                ) {
                    Text(text = state.cursor.toElapsedTime())
                }
                Spacer(modifier = Modifier.width(30.dp))
                IconButton(
                    onClick = { onAction(ActiveSessionAction.PlayPauseClicked) }
                ) {
                    Icon(
                        modifier = Modifier.size(36.dp),
                        painter = painterResource(
                            if (state.isRunning) {
                                R.drawable.pause_circle_outline
                            } else {
                                R.drawable.play_circle_outline
                            }
                        ),
                        contentDescription = "play pause"
                    )
                }
            }
            PreSelectedTagsList(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.6f),
                state = state,
                onAction = onAction,
                snackbarHostState = snackbarHostState
            )
            PreSelectedPlacesList(
                places = state.places.filter { place ->
                    state.preSelectedPlaces.map { it.placeId }.contains(place.id)
                },
                currentPlace = state.currentPlaceName,
                onAction = onAction
            )
            PreSelectedPersonsList(
                persons = state.persons.filter { person ->
                    state.preSelectedPersons.map { it.personId }.contains(person.id)
                },
                currentPerson = state.currentPersonName,
                onAction = onAction
            )
        }
        AnimatedVisibility(
            visible = state.showTagsDialog,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LabelSelectionDialog(
                state = state,
                onAction = onAction,
            )
        }
        AnimatedVisibility(
            visible = state.showEventTrash,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            EventTrashDialog(
                state = state,
                onAction = onAction,
                snackbarHostState = snackbarHostState
            )
        }
        AnimatedVisibility(
            visible = state.showSessionEditionDialog,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            SessionEditionDialog(
                session = state.currentSession,
                onAction = onAction
            )
        }
        AnimatedVisibility(
            visible = state.showEventEditionDialog,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            EventEditionDialog(
                event = state.currentEvent,
                onAccept = { onAction(ActiveSessionAction.AcceptEventEditionClicked(it)) },
                onDismiss = { onAction(ActiveSessionAction.DismissEventEditionDialog) },
            )
        }
        AnimatedVisibility(
            visible = state.showEventInTrashDialog,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            EventEditionDialog(
                event = state.currentEvent,
                onAccept = { onAction(ActiveSessionAction.DismissEventInTrashDialog) },
                onDismiss = { onAction(ActiveSessionAction.DismissEventInTrashDialog) },
                enabled = false
            )
        }
        AnimatedVisibility(
            visible = state.showTimeDialog,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            val max = maxOf(state.cursor, state.currentSession.durationMillis)
            TimeDialog(
                current = state.cursor.toFloat(),
                maximum = max.toFloat(),
                onDismiss = { onAction(ActiveSessionAction.DismissTimeDialog) },
                onAccept = {
                    onAction(ActiveSessionAction.SetCursor(it.toLong()))
                    onAction(ActiveSessionAction.DismissTimeDialog)
                }
            )
        }
    }
}

private const val TAG = "ActiveSessionContent"
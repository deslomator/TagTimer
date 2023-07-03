package com.deslomator.tagtimer.ui.active.session

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.toElapsedTime
import com.deslomator.tagtimer.ui.active.PreSelectedPersonsList
import com.deslomator.tagtimer.ui.active.PreSelectedPlacesList
import com.deslomator.tagtimer.ui.active.dialog.EventEditionDialog
import com.deslomator.tagtimer.ui.active.dialog.TimeDialog
import com.deslomator.tagtimer.ui.theme.VeryLightGray
import kotlinx.coroutines.delay

@Composable
fun ActiveSessionContent(
    paddingValues: PaddingValues,
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val listState = rememberLazyListState()
    BackHandler(enabled = state.showTimeDialog) {
        onAction(ActiveSessionAction.DismissTimeDialog)
    }
    BackHandler(enabled = state.showEventEditionDialog) {
        onAction(ActiveSessionAction.DismissEventEditionDialog)
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
    /*
    this effect launches twice as needed but at least it's not very frequent
     */
    LaunchedEffect(state.eventForScrollTo, state.events) {
        val index = state.events.map { it.id }.indexOf(state.eventForScrollTo.id)
//        Log.d(TAG, "events changed, current event index is: $index")
        if (index >= 0) {
            listState.animateScrollToItem(index, 0)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Divider()
            EventList(
                modifier = Modifier
                    .weight(0.5F),
                events = state.events,
                listState = listState,
                onItemClicked = { onAction(ActiveSessionAction.EventClicked(it)) },
                onItemSwiped = { onAction(ActiveSessionAction.TrashEventSwiped(it)) },
                snackbarHostState = snackbarHostState
            )
            Divider()
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(VeryLightGray)
                    .fillMaxWidth(.9F),
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
                onItemClicked = {
                    onAction(ActiveSessionAction.PreSelectedTagClicked(it))
                },
                snackbarHostState = snackbarHostState
            )
            Divider()
            PreSelectedPersonsList(
                persons = state.persons.filter { person ->
                    state.preSelectedPersons.map { it.labelId }.contains(person.id) &&
                            person.name.isNotEmpty()
                },
                currentPerson = state.currentPersonName,
                onItemClick = {
                    onAction(ActiveSessionAction.PreSelectedPersonClicked(it))
                }
            )
            Divider()
            PreSelectedPlacesList(
                places = state.places.filter { place ->
                    state.preSelectedPlaces.map { it.labelId }.contains(place.id) &&
                            place.name.isNotEmpty()
                },
                currentPlace = state.currentPlaceName,
                onItemClick = {
                    onAction(ActiveSessionAction.PreSelectedPlaceClicked(it))
                }
            )
        }
        AnimatedVisibility(
            visible = state.showEventEditionDialog,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            EventEditionDialog(
                event = state.eventForDialog,
                onAccept = { onAction(ActiveSessionAction.AcceptEventEditionClicked(it)) },
                onDismiss = { onAction(ActiveSessionAction.DismissEventEditionDialog) },
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
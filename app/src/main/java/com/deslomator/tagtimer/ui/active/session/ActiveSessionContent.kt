package com.deslomator.tagtimer.ui.active.session

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
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.action.SharedAction
import com.deslomator.tagtimer.model.type.Sort
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.state.SharedState
import com.deslomator.tagtimer.ui.active.PreSelectedLabelsList
import com.deslomator.tagtimer.ui.active.dialog.EventEditionDialog
import com.deslomator.tagtimer.ui.active.dialog.TimeDialog
import com.deslomator.tagtimer.ui.showSnackbar
import com.deslomator.tagtimer.ui.theme.VeryLightGray
import com.deslomator.tagtimer.ui.theme.hue
import com.deslomator.tagtimer.util.toColor
import com.deslomator.tagtimer.util.toElapsedTime

@Composable
fun ActiveSessionContent(
    paddingValues: PaddingValues,
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit,
    sharedState: SharedState,
    onSharedAction: (SharedAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val listState = rememberLazyListState()
    LaunchedEffect(state.eventForScrollTo, state.events) {
        val index = state.events.map { it.id }.indexOf(state.eventForScrollTo.id)
        if (index >= 0) {
            listState.animateScrollToItem(index, 0)
        }
    }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val tags by remember(sharedState.tagSort, state.tags, state.preSelectedTags) {
        derivedStateOf {
            state.tags.filter { tag ->
                state.preSelectedTags.map { it.labelId }.contains(tag.id)
            }.sortedWith(
                when (sharedState.tagSort) {
                    Sort.COLOR -> compareBy { it.color.toColor().hue() }
                    Sort.NAME -> compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
                }
            )
        }
    }
    val persons by remember(sharedState.personSort, state.persons, state.preSelectedPersons) {
        derivedStateOf {
            state.persons.filter { person ->
                state.preSelectedPersons.map { it.labelId }.contains(person.id)
            }.sortedWith(
                when (sharedState.personSort) {
                    Sort.COLOR -> compareBy { it.color.toColor().hue() }
                    Sort.NAME -> compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
                }
            )
        }
    }
    val places by remember(sharedState.placeSort, state.places, state.preSelectedPlaces) {
        derivedStateOf {
            state.places.filter { place ->
                state.preSelectedPlaces.map { it.labelId }.contains(place.id)
            }.sortedWith(
                when (sharedState.placeSort) {
                    Sort.COLOR -> compareBy { it.color.toColor().hue() }
                    Sort.NAME -> compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
                }
            )
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
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .fillMaxWidth(.9F),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { onAction(ActiveSessionAction.TimeClicked(sharedState.cursor)) }
                ) {
                    Text(text = sharedState.cursor.toElapsedTime())
                }
                Spacer(modifier = Modifier.width(30.dp))
                IconButton(
                    onClick = {
                        onSharedAction(SharedAction.PlayPauseClicked)
                        onAction(ActiveSessionAction.PlayPauseClicked)
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        modifier = Modifier.size(36.dp),
                        painter = painterResource(
                            if (sharedState.isRunning) {
                                R.drawable.pause_circle_outline
                            } else {
                                R.drawable.play_circle_outline
                            }
                        ),
                        contentDescription = "play pause"
                    )
                }
            }
            TagsList(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.6f),
                tags = tags,
                onItemClicked = {
                    if (!sharedState.isRunning) {
                        showSnackbar(
                            scope,
                            snackbarHostState,
                            context.getString(R.string.tap_play_to_add_event)
                        )
                    } else {
                        onAction(ActiveSessionAction.PreSelectedTagClicked(it, sharedState.cursor))
                    }
                },
            )
            Divider()
            PreSelectedLabelsList(
                labels = persons,
                currentLabel = state.currentPersonName,
                onItemClick = {
                    onAction(ActiveSessionAction.PreSelectedPersonClicked(it))
                }
            )
            Divider()
            PreSelectedLabelsList(
                labels = places,
                currentLabel = state.currentPlaceName,
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
            val max = maxOf(sharedState.cursor, state.currentSession.durationMillis)
            TimeDialog(
                current = sharedState.cursor.toFloat(),
                maximum = max.toFloat(),
                onDismiss = { onAction(ActiveSessionAction.DismissTimeDialog) },
                onAccept = {
                    onSharedAction(SharedAction.SetCursor(it.toLong()))
                    onAction(ActiveSessionAction.DismissTimeDialog)
                }
            )
        }
    }
}

private const val TAG = "ActiveSessionContent"
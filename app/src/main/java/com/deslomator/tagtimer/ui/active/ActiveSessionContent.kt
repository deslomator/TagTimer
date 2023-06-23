package com.deslomator.tagtimer.ui.active

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.state.ActiveSessionState

@Composable
fun ActiveSessionContent(
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit
) {
    BackHandler(enabled = state.showTagsDialog) {
        onAction(ActiveSessionAction.DismissTagDialog)
    }
    BackHandler(enabled = state.showEventTrash) {
        onAction(ActiveSessionAction.DismissEventTrashDialog)
    }
    Box {
        Column {
            EventList(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.4f),
                state = state,
                onAction = onAction
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
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
                onAction = onAction
            )
        }
        AnimatedVisibility(
            visible = state.showTagsDialog,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            TagSelectionDialog(
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
                onAction = onAction
            )
        }
        AnimatedVisibility(
            visible = state.showSessionEditionDialog,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            SessionEditionDialog(
                state = state,
                onAction = onAction
            )
        }
    }
}

private const val TAG = "ActiveSessionContent"
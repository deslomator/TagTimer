package com.deslomator.tagtimer.ui.active

import android.content.Intent
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.toElapsedTime
import kotlinx.coroutines.delay
import java.io.File

@Composable
fun ActiveSessionContent(
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    BackHandler(enabled = state.showTagsDialog) {
        onAction(ActiveSessionAction.DismissTagDialog)
    }
    BackHandler(enabled = state.showEventTrash) {
        onAction(ActiveSessionAction.DismissEventTrashDialog)
    }
    val context = LocalContext.current
    var runTimer by remember { mutableStateOf(false)}
    var ticks by remember { mutableStateOf(state.currentSession.durationMillis) }
    LaunchedEffect(state.currentSession) {
        ticks = state.currentSession.durationMillis
    }
    LaunchedEffect(runTimer) {
        if (runTimer) {
            ticks = state.currentSession.durationMillis
            while (true) {
                delay(1000)
                ticks += 1000
            }
        }
    }
    if (state.exportSession) {
        LaunchedEffect(Unit) {
            try {
                val file = File(
                    context.cacheDir,
                    "${state.currentSession.name}.$JSON_EXTENSION"
                )
                file.writeBytes(state.sessionToExport.encodeToByteArray())
                val uri = FileProvider.getUriForFile(
                    context,
                    FILE_PROVIDER,
                    file
                )
                val intent = Intent(Intent.ACTION_SEND)
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    .setType("application/json")
                    .putExtra(Intent.EXTRA_STREAM, uri)
                Intent.createChooser(intent, "Choose an App")
                startActivity(context, intent, null)
                onAction(ActiveSessionAction.SessionExported)
            } catch (error: Error) {
                Log.d(TAG, "ShareSession, error: $error")
            }
        }
    }
    Box {
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
//                Text(state.currentSession.durationMillis.toElapsedTime())
                Text(ticks.toElapsedTime())
                IconButton(
                    onClick = {
                        runTimer = !state.isRunning
                        onAction(ActiveSessionAction.PlayPauseClicked) }
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
                onDismiss = { onAction(ActiveSessionAction.DismissSessionEditionDialog) },
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
    }
}

private const val TAG = "ActiveSessionContent"
private const val JSON_EXTENSION = "json"
const val FILE_PROVIDER = "com.deslomator.tagtimer.fileprovider"
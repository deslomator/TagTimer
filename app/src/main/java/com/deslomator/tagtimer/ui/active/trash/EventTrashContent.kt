package com.deslomator.tagtimer.ui.active.trash

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.ui.active.EventListItem
import com.deslomator.tagtimer.ui.active.dialog.EventEditionDialog
import com.deslomator.tagtimer.ui.showSnackbar

@Composable
fun EventTrashContent(
    paddingValues: PaddingValues,
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    BackHandler(enabled = state.showEventInTrashDialog) {
        onAction(ActiveSessionAction.DismissEventInTrashDialog)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(6.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            if (state.trashedEvents.isEmpty()) {
                item {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = stringResource(id = R.string.event_trash_is_empty)
                    )
                }
            }
            items(
                items = state.trashedEvents,
                key = { it.id }
            ) { event ->
                EventListItem(
                    event = event,
                    leadingIcon = R.drawable.restore_from_trash,
                    onLeadingClick = {
                        showSnackbar(
                            scope,
                            snackbarHostState,
                            context.getString(R.string.event_restored)
                        )
                        onAction(ActiveSessionAction.RestoreEventClicked(event))
                    },
                    trailingIcon = R.drawable.delete_forever,
                    onTrailingClick = {
                        showSnackbar(
                            scope,
                            snackbarHostState,
                            context.getString(R.string.event_deleted)
                        )
                        onAction(ActiveSessionAction.DeleteEventClicked(event))
                    },
                    onItemClick = { onAction(ActiveSessionAction.EventInTrashClicked(event)) },
                )
            }
        }
    }
    AnimatedVisibility(
        visible = state.showEventInTrashDialog,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        EventEditionDialog(
            event = state.eventForDialog,
            onAccept = { onAction(ActiveSessionAction.DismissEventInTrashDialog) },
            onDismiss = { onAction(ActiveSessionAction.DismissEventInTrashDialog) },
            enabled = false
        )
    }
}

private const val TAG = "EventTrash"
package com.deslomator.tagtimer.ui.active.trash

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.TrashTabAction
import com.deslomator.tagtimer.state.TrashTabState
import com.deslomator.tagtimer.ui.active.EventListItem
import com.deslomator.tagtimer.ui.active.dialog.EventEditionDialog
import com.deslomator.tagtimer.ui.showSnackbar
import kotlinx.coroutines.CoroutineScope

@Composable
fun EventTrash(
    state: TrashTabState,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    context: Context,
    onAction: (TrashTabAction) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
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
                key = { it.event.id!! }
            ) { event4d ->
                EventListItem(
                    event4d = event4d,
                    leadingIcon = R.drawable.restore_from_trash,
                    onLeadingClick = {
                        showSnackbar(
                            scope,
                            snackbarHostState,
                            context.getString(R.string.event_restored)
                        )
                        onAction(TrashTabAction.RestoreEventClicked(event4d))
                    },
                    trailingIcon = R.drawable.delete_forever,
                    onTrailingClick = {
                        showSnackbar(
                            scope,
                            snackbarHostState,
                            context.getString(R.string.event_deleted)
                        )
                        onAction(TrashTabAction.DeleteEventClicked(event4d))
                    },
                    onItemClick = { onAction(TrashTabAction.EventInTrashClicked(event4d)) },
                )
            }
        }
        AnimatedVisibility(
            visible = state.showEventInTrashDialog,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            EventEditionDialog(
                event4d = state.eventForDialog,
                onAccept = { onAction(TrashTabAction.DismissEventInTrashDialog) },
                onDismiss = { onAction(TrashTabAction.DismissEventInTrashDialog) },
                enabled = false
            )
        }
    }
}
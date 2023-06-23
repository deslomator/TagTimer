package com.deslomator.tagtimer.ui.active

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.ui.MyDialog

@Composable
fun EventTrashDialog(
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit
) {
    MyDialog(
        onDismiss = { onAction(ActiveSessionAction.DismissEventTrashDialog) }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
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
                    onLeadingClick = { onAction(ActiveSessionAction.RestoreEventClicked(event)) },
                    trailingIcon = R.drawable.delete_forever,
                    onTrailingClick = { onAction(ActiveSessionAction.DeleteEventClicked(event)) },
                    noteEnabled = false,
                )
            }
        }
    }
}

private const val TAG = "EventTrashDialog"
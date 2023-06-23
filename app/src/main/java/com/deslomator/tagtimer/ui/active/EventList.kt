package com.deslomator.tagtimer.ui.active

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.ui.SwipeableListItem
import com.deslomator.tagtimer.ui.theme.Pink80

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun EventList(
    modifier: Modifier,
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        item {
            if (state.events.isEmpty()) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.tap_a_tag_below),
                    textAlign = TextAlign.Center
                )
            }
        }
        items(
            items = state.events,
            key = { it.id }
        ) { event ->
            Log.d(TAG, "item note: ${event.note}, id: ${event.id}, category: ${event.category}, label: ${event.label}")
            SwipeableListItem(
                dismissDirection = DismissDirection.StartToEnd,
                onDismiss = {
                    Log.d(TAG, "onDismiss() note: ${event.note}, id: ${event.id}, category: ${event.category}, label: ${event.label}")
                    onAction(ActiveSessionAction.TrashEventSwiped(event))
                },
                dismissColor = Pink80
            ) {
                EventListItem(
                    event = event,
                    onAcceptEventNote = {
                        onAction(ActiveSessionAction.AcceptEventNoteChanged(event, it))
                    }
                )
            }
        }
    }
}

private const val TAG = "EventList"
package com.deslomator.tagtimer.ui.active

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.ui.SwipeableListItem
import com.deslomator.tagtimer.ui.main.trash.showSnackbar
import com.deslomator.tagtimer.ui.theme.Pink80

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun EventList(
    modifier: Modifier,
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    var deleting by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(state.events.size) {
        if (!deleting)
            listState.animateScrollToItem(maxOf(0, state.events.size - 1))
        deleting = false
    }
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(6.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        state = listState
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
                    showSnackbar(
                        scope,
                        snackbarHostState,
                        context.getString(R.string.event_sent_to_trash)
                    )
                    Log.d(TAG, "onDismiss() note: ${event.note}, id: ${event.id}, category: ${event.category}, label: ${event.label}")
                    deleting = true
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
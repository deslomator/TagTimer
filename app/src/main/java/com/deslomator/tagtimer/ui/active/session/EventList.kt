package com.deslomator.tagtimer.ui.active.session

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.EventForDisplay
import com.deslomator.tagtimer.ui.EmptyListText
import com.deslomator.tagtimer.ui.SwipeableListItem
import com.deslomator.tagtimer.ui.active.EventListItem
import com.deslomator.tagtimer.ui.showSnackbar

@Composable
fun EventList(
    modifier: Modifier,
    events: List<EventForDisplay>,
    listState: LazyListState,
    onItemClicked: (EventForDisplay) -> Unit,
    onItemSwiped: (EventForDisplay) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val empty by remember(events.size) { derivedStateOf { events.isEmpty() } }
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(6.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        state = listState
    ) {
        item {
            if (empty) {
                EmptyListText(stringResource(id = R.string.tap_a_tag_below))
            }
        }
        items(
            items = events,
            key = { it.event.id!! }
        ) { event4d ->
            SwipeableListItem(
                onDismiss = {
                    showSnackbar(
                        scope,
                        snackbarHostState,
                        context.getString(R.string.event_sent_to_trash)
                    )
                    onItemSwiped(event4d)
                },
            ) {
                EventListItem(
                    event4d = event4d,
                    trailingIcon = if (event4d.event.note.isEmpty()) null else R.drawable.note,
                    onTrailingClick = { onItemClicked(event4d) },
                    onItemClick = { onItemClicked(event4d) },
                    persons = events.mapNotNull{ it.getPersonName() }.distinct().sorted()
                )
            }
        }
    }
}

private const val TAG = "EventList"
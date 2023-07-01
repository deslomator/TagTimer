package com.deslomator.tagtimer.ui.active

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.ui.SwipeableListItem
import com.deslomator.tagtimer.ui.showSnackbar
import com.deslomator.tagtimer.ui.theme.Pink80

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun EventList(
    modifier: Modifier,
    events: List<Event>,
    listState: LazyListState,
    onItemClicked: (Event) -> Unit,
    onItemSwiped: (Event) -> Unit,
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
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.tap_a_tag_below),
                    textAlign = TextAlign.Center
                )
            }
        }
        items(
            items = events,
            key = { it.id }
        ) { event ->
            SwipeableListItem(
                dismissDirection = DismissDirection.StartToEnd,
                onDismiss = {
                    showSnackbar(
                        scope,
                        snackbarHostState,
                        context.getString(R.string.event_sent_to_trash)
                    )
                    onItemSwiped(event)
                },
                dismissColor = Pink80
            ) {
                EventListItem(
                    event = event,
                    trailingIcon = if (event.note.isEmpty()) null else R.drawable.note,
                    onTrailingClick = { onItemClicked(event) },
                    onItemClick = { onItemClicked(event) },
                    persons = events.map { it.person }.distinct().sorted()
                )
            }
        }
    }
}

private const val TAG = "EventList"
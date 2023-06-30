package com.deslomator.tagtimer.ui.active.filter

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Person
import com.deslomator.tagtimer.model.Place
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.ui.active.EventListItem
import com.deslomator.tagtimer.ui.active.PreSelectedPersonsList
import com.deslomator.tagtimer.ui.active.PreSelectedPlacesList
import com.deslomator.tagtimer.ui.active.dialog.EventEditionDialog

@Composable
fun EventFilterContent(
    paddingValues: PaddingValues,
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit,
    persons: List<Person>,
    places: List<Place>,
    query: String,
    onQueryChange: (String) -> Unit,
    filteredEvents: List<Event>
) {
    BackHandler(enabled = state.showEventEditionDialog) {
        onAction(ActiveSessionAction.DismissEventEditionDialog)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        Column {
            PreSelectedPersonsList(
                persons = persons,
                currentPerson = state.currentPersonName,
                onAction = onAction
            )
            PreSelectedPlacesList(
                places = places,
                currentPlace = state.currentPlaceName,
                onAction = onAction
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(20.dp))
                TextField(
                    modifier = Modifier.weight(1F),
                    value = query,
                    onValueChange = { onQueryChange(it) },
                    placeholder = { Text(stringResource(R.string.search_for_tag)) },
                    shape = RoundedCornerShape(10.dp)
                )
                Spacer(modifier = Modifier.width(40.dp))
                Text(
                    text = stringResource(R.string.total_events, filteredEvents.size),
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.width(20.dp))
            }
            LazyColumn(
                modifier = Modifier.weight(1F),
                contentPadding = PaddingValues(6.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                items(
                    items = filteredEvents,
                    key = { it.id }
                ) { event ->
                    EventListItem(
                        event = event,
                        trailingIcon = if (event.note.isEmpty()) null else R.drawable.note,
                        onTrailingClick = { onAction(ActiveSessionAction.EventClicked(event)) },
                        onItemClick = { onAction(ActiveSessionAction.EventClicked(event)) },
                        persons = state.events.map { it.person }.distinct().sorted()
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = state.showEventEditionDialog,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            EventEditionDialog(
                event = state.currentEvent,
                onAccept = { onAction(ActiveSessionAction.AcceptEventEditionClicked(it)) },
                onDismiss = { onAction(ActiveSessionAction.DismissEventEditionDialog) },
            )
        }
    }
}

private const val TAG = "EventFilterContent"
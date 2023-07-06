package com.deslomator.tagtimer.ui.active.filter

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.EventFilterAction
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.type.Sort
import com.deslomator.tagtimer.state.EventFilterState
import com.deslomator.tagtimer.state.SharedState
import com.deslomator.tagtimer.ui.active.EventListItem
import com.deslomator.tagtimer.ui.active.PreSelectedPersonsList
import com.deslomator.tagtimer.ui.active.PreSelectedPlacesList
import com.deslomator.tagtimer.ui.active.dialog.EventEditionDialog
import com.deslomator.tagtimer.ui.theme.hue

@Composable
fun EventFilterContent(
    paddingValues: PaddingValues,
    state: EventFilterState,
    sharedState: SharedState,
    onAction: (EventFilterAction) -> Unit,
    filteredEvents: List<Event>
) {
    val tags = state.tags
        .filter { tag ->
            tag.name.isNotEmpty() &&
                    state.events.map { it.tag }.contains(tag.name)
        }.distinctBy { it.name }
        .sortedWith(
            when (sharedState.tagSort) {
                Sort.COLOR -> compareBy { Color(it.color).hue() }
                Sort.NAME -> compareBy { it.name }
            }
        )
    val persons = state.persons
        .filter { person ->
            person.name.isNotEmpty() &&
                    state.events.map { it.person }.contains(person.name)
        }.distinctBy { it.name }
        .sortedWith(
            when (sharedState.personSort) {
                Sort.COLOR -> compareBy { Color(it.color).hue() }
                Sort.NAME -> compareBy { it.name }
            }
        )
    val places = state.places
        .filter { place ->
            place.name.isNotEmpty() &&
                    state.events.map { it.place }.contains(place.name)
        }.distinctBy { it.name }
        .sortedWith(
            when (sharedState.placeSort) {
                Sort.COLOR -> compareBy { Color(it.color).hue() }
                Sort.NAME -> compareBy { it.name }
            }
        )
    BackHandler(enabled = state.showEventEditionDialog) {
        onAction(EventFilterAction.DismissEventEditionDialog)
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
                onItemClick = { onAction(EventFilterAction.PreSelectedPersonClicked(it)) }
            )
            Divider()
            PreSelectedPlacesList(
                places = places,
                currentPlace = state.currentPlaceName,
                onItemClick = { onAction(EventFilterAction.PreSelectedPlaceClicked(it)) }
            )
            Divider()
            UsedTagsList(
                modifier = Modifier.weight(.28F),
                tags = tags,
                currentTag = state.currentLabelName,
                onItemClick = { onAction(EventFilterAction.UsedTagClicked(it)) }
            )
            Divider()
            LazyColumn(
                modifier = Modifier.weight(.4F),
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
                        onTrailingClick = { onAction(EventFilterAction.EventClicked(event)) },
                        onItemClick = { onAction(EventFilterAction.EventClicked(event)) },
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
                event = state.eventForDialog,
                onAccept = { onAction(EventFilterAction.AcceptEventEditionClicked(it)) },
                onDismiss = { onAction(EventFilterAction.DismissEventEditionDialog) },
            )
        }
    }
}

private const val TAG = "EventFilterContent"
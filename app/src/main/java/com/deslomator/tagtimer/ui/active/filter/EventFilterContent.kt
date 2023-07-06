package com.deslomator.tagtimer.ui.active.filter

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
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
    val tags = remember(state.tags, state.events) {
        state.tags
            .filter { tag ->
                tag.name.isNotEmpty() &&
                        state.events.map { it.tag }.contains(tag.name)
            }.distinctBy { it.name }
            .sortedWith(
                when (sharedState.tagSort) {
                    Sort.COLOR -> compareBy { Color(it.color).hue() }
                    Sort.NAME -> compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
                }
            )
    }
    val persons = remember(state.persons, state.events) {
        state.persons
            .filter { person ->
                person.name.isNotEmpty() &&
                        state.events.map { it.person }.contains(person.name)
            }.distinctBy { it.name }
            .sortedWith(
                when (sharedState.personSort) {
                    Sort.COLOR -> compareBy { Color(it.color).hue() }
                    Sort.NAME -> compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
                }
            )
    }
    val places = remember(state.places, state.events) {
        state.places
            .filter { place ->
                place.name.isNotEmpty() &&
                        state.events.map { it.place }.contains(place.name)
            }.distinctBy { it.name }
            .sortedWith(
                when (sharedState.placeSort) {
                    Sort.COLOR -> compareBy { Color(it.color).hue() }
                    Sort.NAME -> compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
                }
            )
    }
    val infoText by remember(
        state.currentPersonName,
        state.currentPlaceName,
        state.currentTagName
    ) {
        derivedStateOf {
            listOf(
                state.currentPersonName,
                state.currentPlaceName,
                state.currentTagName
            ).filter { it.isNotEmpty() }.joinToString(", ")
        }
    }
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
            AnimatedVisibility(
                visible = infoText.isNotEmpty(),
                enter = slideInVertically(),
                exit = slideOutVertically()
            ) {
                Divider()
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = infoText,
                    textAlign = TextAlign.Center
                )
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
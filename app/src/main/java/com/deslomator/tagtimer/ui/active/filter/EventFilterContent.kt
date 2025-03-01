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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.EventFilterAction
import com.deslomator.tagtimer.model.EventForDisplay
import com.deslomator.tagtimer.state.EventFilterState
import com.deslomator.tagtimer.ui.EmptyListText
import com.deslomator.tagtimer.ui.active.EventListItem
import com.deslomator.tagtimer.ui.active.PreSelectedLabelsList
import com.deslomator.tagtimer.ui.active.dialog.EventEditionDialog
import com.deslomator.tagtimer.ui.active.session.TagsList

@Composable
fun EventFilterContent(
    paddingValues: PaddingValues,
    state: EventFilterState,
    onAction: (EventFilterAction) -> Unit,
    filteredEvents: List<EventForDisplay>
) {
    BackHandler(enabled = state.showEventEditionDialog) {
        onAction(EventFilterAction.DismissEventEditionDialog)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        Column {
            PreSelectedLabelsList(
                labels = state.persons,
                currentLabel = state.currentPerson,
                onItemClick = { onAction(EventFilterAction.UsedPersonClicked(it)) }
            )
            HorizontalDivider()
            PreSelectedLabelsList(
                labels = state.places,
                currentLabel = state.currentPlace,
                onItemClick = { onAction(EventFilterAction.UsedPlaceClicked(it)) }
            )
            HorizontalDivider()
            TagsList(
                modifier = Modifier.weight(.28F),
                tags = state.tags,
                currentTags = state.currentTags,
                onItemClicked = { onAction(EventFilterAction.UsedTagClicked(it)) },
                showChecked = true
            )
            HorizontalDivider()
            LazyColumn(
                modifier = Modifier.weight(.4F),
                contentPadding = PaddingValues(6.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                items(
                    items = filteredEvents,
                    key = { it.event.id!! }
                ) { event4d ->
                    EventListItem(
                        event4d = event4d,
                        trailingIcon = if (event4d.event.note.isEmpty()) null else R.drawable.note,
                        onTrailingClick = { onAction(EventFilterAction.EventClicked(event4d)) },
                        onItemClick = { onAction(EventFilterAction.EventClicked(event4d)) },
                        persons = filteredEvents.map { it.getPersonName() }.filterNotNull().distinct().sorted()
                    )
                }
            }
            AnimatedVisibility(
                visible = state.query.isNotEmpty(),
                enter = slideInVertically(),
                exit = slideOutVertically()
            ) {
                HorizontalDivider()
                EmptyListText(state.query)
            }
        }
        AnimatedVisibility(
            visible = state.showEventEditionDialog,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            EventEditionDialog(
                event4d = state.eventForDialog,
                onAccept = { onAction(EventFilterAction.AcceptEventEditionClicked(it)) },
                onDismiss = { onAction(EventFilterAction.DismissEventEditionDialog) },
            )
        }
    }
}

private const val TAG = "EventFilterContent"
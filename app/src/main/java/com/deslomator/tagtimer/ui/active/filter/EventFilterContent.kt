package com.deslomator.tagtimer.ui.active.filter

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.EventFilterAction
import com.deslomator.tagtimer.model.ancillary.EventForDisplay
import com.deslomator.tagtimer.state.EventFilterState
import com.deslomator.tagtimer.ui.EmptyListText
import com.deslomator.tagtimer.ui.active.EventListItem2
import com.deslomator.tagtimer.ui.active.SelectedLabelsList
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
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(top = 10.dp)
    ) {
        SelectedLabelsList(
            labels = state.persons,
            currentLabel = state.currentPerson,
            onItemClick = { onAction(EventFilterAction.UsedPersonClicked(it)) }
        )
        HorizontalDivider()
        SelectedLabelsList(
            labels = state.places,
            currentLabel = state.currentPlace,
            onItemClick = { onAction(EventFilterAction.UsedPlaceClicked(it)) }
        )
        HorizontalDivider()
        TagsList(
            modifier = Modifier.weight(.28F),
            tags = state.tags,
            onItemClicked = { onAction(EventFilterAction.UsedTagClicked(it)) },
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
                EventListItem2(
                    event4d = event4d,
                    trailingIcon = if (event4d.event.note.isEmpty()) null else R.drawable.note,
                    onTrailingClick = { onAction(EventFilterAction.EventClicked(event4d)) },
                    onItemClick = { onAction(EventFilterAction.EventClicked(event4d)) },
                    persons = filteredEvents.mapNotNull { it.person?.name }.distinct().sorted()
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
}

private const val TAG = "EventFilterContent"
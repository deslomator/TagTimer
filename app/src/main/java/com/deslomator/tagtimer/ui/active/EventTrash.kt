package com.deslomator.tagtimer.ui.active

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.state.ActiveSessionState

@Composable
fun EventTrash(
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit
) {
    Box(
        modifier = Modifier
            .background(Color.Black.copy(alpha = .65f))
            .clickable { onAction(ActiveSessionAction.DismissEventTrashDialog) }
            .fillMaxSize()
            .padding(top = 50.dp, bottom = 50.dp, start = 16.dp, end = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(10.dp))
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(top = 5.dp, bottom = 5.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1.0f),
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { onAction(ActiveSessionAction.DismissEventTrashDialog) }
                    ) {
                        Text(text = stringResource(id = R.string.accept))
                    }
                }
            }
        }
    }
}

private const val TAG = "EventTrash"
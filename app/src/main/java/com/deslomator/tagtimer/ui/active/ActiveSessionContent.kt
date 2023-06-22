package com.deslomator.tagtimer.ui.active

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.ui.MyListItem
import com.deslomator.tagtimer.ui.theme.contrasted

@Composable
fun ActiveSessionContent(
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit
) {
    BackHandler(enabled = state.showTagsDialog) {
        onAction(ActiveSessionAction.DismissTagDialog)
    }
    BackHandler(enabled = state.showEventTrash) {
        onAction(ActiveSessionAction.DismissEventTrashDialog)
    }
    Box {
        Column {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.4f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                item {
                    if (state.events.isEmpty()) {
                        Text(text = stringResource(id = R.string.tap_a_tag_below))
                    }
                }
                items(
                    items = state.events,
                    key = { it.id }
                ) { event ->
                    EventListItem(
                        event,
                        onAction
                    )
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.6f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                item {
                    if (state.preSelectedTags.isEmpty()) {
                        Text(text = stringResource(id = R.string.tap_toolbar_icon_add_pst))
                    }
                }
                items(
                    items = state.tags.filter { tag ->
                        state.preSelectedTags.map { it.tagId }.contains(tag.id)
                    },
                    key = { it.id }
                ) { tag ->
                    MyListItem(
                        modifier = Modifier
                            .clip(CutCornerShape(topStart = 20.dp))
                            .border(1.dp, Color.LightGray, CutCornerShape(topStart = 20.dp)),
                        leadingIcon = R.drawable.tag,
                        colors = ListItemDefaults.colors(
                            leadingIconColor = Color(tag.color).contrasted(),
                            headlineColor = Color(tag.color).contrasted(),
                            trailingIconColor = Color(tag.color).contrasted(),
                            containerColor = Color(tag.color),
                        ),
                        item = tag,
                        onItemClick = { onAction(ActiveSessionAction.PreSelectedTagClicked(tag)) },
                    ) { item ->
                        Column {
                            Text(item.label)
                            Text(item.category)
                        }
                    }
                }
            }
        }
        if (state.showTagsDialog) {
            TagSelectionDialog(
                state = state,
                onAction = onAction,
            )
        }
        if (state.showEventTrash) {
            Surface(
                modifier = Modifier
                    .clickable { onAction(ActiveSessionAction.DismissEventTrashDialog) }
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = .65f))
                    .padding(top = 50.dp, bottom = 50.dp, start = 16.dp, end = 16.dp)
                    .clip(shape = RoundedCornerShape(10.dp))
            ) {
                Column(
                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                ) {
                    Text("Event Trash")
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
}

private const val TAG = "ActiveSessionContent"
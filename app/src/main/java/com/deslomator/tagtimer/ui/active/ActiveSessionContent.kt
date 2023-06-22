package com.deslomator.tagtimer.ui.active

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    Box {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            item {
                if (state.preSelectedTags.isEmpty()) {
                    Text(text = "Add some Tag pressing the icon")
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
                    onItemClick = { onAction(ActiveSessionAction.ActiveSessionClicked(tag)) },
                ) { item ->
                    Column {
                        Text(item.label)
                        Text(item.category)
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
    }
}

private const val TAG = "ActiveSessionContent"
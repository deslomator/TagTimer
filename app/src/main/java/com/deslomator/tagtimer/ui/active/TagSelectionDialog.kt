package com.deslomator.tagtimer.ui.active

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.ui.theme.contrasted

@Composable
fun TagSelectionDialog(
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit,
) {

    AlertDialog(
        modifier = Modifier.fillMaxWidth(.8f),
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = {
            onAction(ActiveSessionAction.DismissTagDialog)
        },
        title = {
            Text(
                text = stringResource(id = R.string.select_tags)
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(
                    items = state.tags,
                    key = { it.id }
                ) { tag ->
                    SelectedTagListItem(
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
                        checked = state.preSelectedTags.map { it.tagId }.contains(tag.id),
                        onCheckedChange = {
//                            Log.d(TAG, "SelectTagCheckedChange: $it")
                            onAction(ActiveSessionAction.SelectTagCheckedChange(tag.id, it))
                        }
                        /*trailingIcon = R.drawable.baseline_edit_24,
                        onTrailingClick = { onAction(TagsScreenAction.EditTagClicked(tag)) },
                        shadowElevation = animateDpAsState(
                            if (dismissState.dismissDirection != null) 20.dp else 10.dp
                        ).value*/
                    ) { item ->
                        Column {
                            Text(item.label)
                            Text(item.category)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onAction(ActiveSessionAction.AcceptTagSelectionClicked)
                }
            ) {
                Text(stringResource(id = R.string.accept))
            }
        },
    )
}

private const val TAG = "TagSelectionDialog"
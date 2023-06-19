package com.deslomator.tagtimer.ui.main.tags

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.TagsScreenAction
import com.deslomator.tagtimer.model.Tag
import com.deslomator.tagtimer.state.TagsScreenState
import com.deslomator.tagtimer.ui.ColorPicker

@Composable
fun TagDialog(
    state: TagsScreenState,
    onAction: (TagsScreenAction) -> Unit,
    tag: Tag
) {

    AlertDialog(
        modifier = Modifier.fillMaxWidth(.8f),
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = {
            onAction(TagsScreenAction.DismissTagDialog)
        },
        title = {
            Text(
                text = stringResource(id = if(state.isEditingTag) R.string.edit_tag
                else R.string.new_tag)
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = state.tagLabel,
                    onValueChange = { onAction(TagsScreenAction.UpdateTagLabel(it)) },
                    placeholder = { Text(text = stringResource(id = R.string.label)) }
                )
                TextField(
                    value = state.tagCategory,
                    onValueChange = { onAction(TagsScreenAction.UpdateTagCategory(it)) },
                    placeholder = { Text(text = stringResource(id = R.string.category)) }
                )
                ColorPicker(
                    selectedColor = Color(state.tagColor),
                    onItemClick = { onAction(TagsScreenAction.UpdateTagColor(it)) }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onAction(TagsScreenAction.AcceptTagEditionClicked(tag))
                }
            ) {
                Text(stringResource(id = R.string.accept))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onAction(TagsScreenAction.DismissTagDialog)
                }
            ) {
                Text(stringResource(id = R.string.cancel))
            }
        }
    )
}

@Composable
@Preview
fun TagDialogPreview() {
    TagDialog(
        state = TagsScreenState(tagColor = 0xff4477),
        onAction = {},
        tag = Tag()
    )
}

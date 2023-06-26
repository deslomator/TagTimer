package com.deslomator.tagtimer.ui.main.tags

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.TagsTabAction
import com.deslomator.tagtimer.model.Tag
import com.deslomator.tagtimer.state.TagsTabState
import com.deslomator.tagtimer.ui.ColorPicker
import com.deslomator.tagtimer.ui.MyDialog

@Composable
fun TagDialog(
    state: TagsTabState,
    onAction: (TagsTabAction) -> Unit,
    tag: Tag
) {
    MyDialog(
        onDismiss = { onAction(TagsTabAction.DismissTagDialog) },
        onAccept = { onAction(TagsTabAction.AcceptTagEditionClicked(tag)) }
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = if(state.isEditingTag) R.string.edit_tag
            else R.string.new_tag),
            textAlign = TextAlign.Center
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.tagLabel,
            onValueChange = { onAction(TagsTabAction.UpdateTagLabel(it)) },
            placeholder = { Text(text = stringResource(id = R.string.label)) }
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.tagCategory,
            onValueChange = { onAction(TagsTabAction.UpdateTagCategory(it)) },
            placeholder = { Text(text = stringResource(id = R.string.category)) }
        )
        ColorPicker(
            selectedColor = Color(state.tagColor),
            onItemClick = { onAction(TagsTabAction.UpdateTagColor(it)) }
        )
    }
}

@Composable
@Preview
fun TagDialogPreview() {
    TagDialog(
        state = TagsTabState(tagColor = 0xff4477),
        onAction = {},
        tag = Tag()
    )
}

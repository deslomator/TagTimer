package com.deslomator.tagtimer.ui.main.labels

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.LabelsTabAction
import com.deslomator.tagtimer.model.Tag
import com.deslomator.tagtimer.state.LabelsTabState
import com.deslomator.tagtimer.ui.ColorPicker
import com.deslomator.tagtimer.ui.MyDialog

@Composable
fun TagDialog(
    state: LabelsTabState,
    onAction: (LabelsTabAction) -> Unit,
    tag: Tag
) {
    var label by rememberSaveable { mutableStateOf(tag.label) }
    var color by rememberSaveable { mutableStateOf(tag.color) }
    MyDialog(
        onDismiss = { onAction(LabelsTabAction.DismissTagDialog) },
        onAccept = {
            val t = tag.copy(
                label = label,
                color = color
            )
            onAction(LabelsTabAction.AcceptTagEditionClicked(t))
        }
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = if(state.isEditingTag) R.string.edit_tag
            else R.string.new_tag),
            textAlign = TextAlign.Center
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = label,
            onValueChange = { label = it },
            placeholder = { Text(text = stringResource(id = R.string.label)) }
        )
        ColorPicker(
            selectedColor = Color(color),
            onItemClick = { color = it }
        )
    }
}

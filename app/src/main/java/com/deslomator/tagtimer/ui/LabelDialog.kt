package com.deslomator.tagtimer.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.ui.theme.toHex
import com.deslomator.tagtimer.util.toColor

@Composable
fun LabelDialog(
    currentLabel: Label,
    onDismiss: () -> Unit,
    onAccept: (Label) -> Unit,
    showTrash: Boolean,
    onTrash: (Label) -> Unit,
    @StringRes title: Int,
    @DrawableRes icon: Int,
) {
    var name by rememberSaveable { mutableStateOf(currentLabel.name) }
    var color by rememberSaveable { mutableStateOf(currentLabel.color) }
    MyDialog(
        onDismiss = onDismiss,
        onAccept = {
            val editedLabel = currentLabel.copy(
                name = name,
                color= color
            )
            onAccept(editedLabel)
        },
        showTrash = showTrash,
        onTrash = { onTrash(currentLabel) },
        title = title
    ) {
        DialogTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = R.string.name,
            icon = icon
        )
        Spacer(modifier = Modifier.height(7.dp))
        ColorPicker(
            selectedColor = color.toColor(),
            onItemClick = { color = it.toHex() }
        )
    }
}
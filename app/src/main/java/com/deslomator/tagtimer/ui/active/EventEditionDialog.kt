package com.deslomator.tagtimer.ui.active

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
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.ui.ColorPicker
import com.deslomator.tagtimer.ui.MyDialog

@Composable
fun EventEditionDialog(
    event: Event,
    onAccept: (Event) -> Unit,
    onDismiss: () -> Unit,
    enabled: Boolean = true
) {
    var elapsed by rememberSaveable {
        mutableStateOf(event.elapsedTimeMillis)
    }
    var label by rememberSaveable {
        mutableStateOf(event.label)
    }
    var category by rememberSaveable {
        mutableStateOf(event.category)
    }
    var note by rememberSaveable {
        mutableStateOf(event.note)
    }
    var color by rememberSaveable {
        mutableStateOf(event.color)
    }
    MyDialog(
        onDismiss = onDismiss,
        onAccept = {
            val e = event.copy(
                elapsedTimeMillis = elapsed,
                label = label,
                category = category,
                note = note,
                color = color
            )
            onAccept(e)
        },
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = elapsed.toString(),
            onValueChange = { elapsed = it.toLong() },
            placeholder = { Text(text = "timestamp") },
            enabled = enabled
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = label,
            onValueChange = { label = it },
            placeholder = { Text(text = stringResource(id = R.string.label)) },
            enabled = enabled
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = category,
            onValueChange = { category = it },
            placeholder = { Text(text = stringResource(id = R.string.category)) },
            enabled = enabled
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = note,
            onValueChange = { note = it },
            placeholder = { Text(text = stringResource(id = R.string.type_a_note)) },
            enabled = enabled
        )
        ColorPicker(
            selectedColor = Color(color),
            onItemClick = { color = it },
            enabled = enabled
        )
    }
}

private const val TAG = "EventEditionDialog"
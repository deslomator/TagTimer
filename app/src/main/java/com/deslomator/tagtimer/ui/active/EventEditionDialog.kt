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
import com.deslomator.tagtimer.ui.TimeNumberPicker

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
    var note by rememberSaveable {
        mutableStateOf(event.note)
    }
    var person by rememberSaveable {
        mutableStateOf(event.person)
    }
    var place by rememberSaveable {
        mutableStateOf(event.place)
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
                note = note,
                person = person,
                place = place,
                color = color
            )
            onAccept(e)
        },
    ) {
        TimeNumberPicker(
            timeMillis = elapsed,
            onValueChange = { elapsed = it }
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
            value = note,
            onValueChange = { note = it },
            placeholder = { Text(text = stringResource(id = R.string.type_a_note)) },
            enabled = enabled
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = person,
            onValueChange = { person = it },
            placeholder = { Text(text = stringResource(id = R.string.person)) },
            enabled = enabled
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = place,
            onValueChange = { place = it },
            placeholder = { Text(text = stringResource(id = R.string.place)) },
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
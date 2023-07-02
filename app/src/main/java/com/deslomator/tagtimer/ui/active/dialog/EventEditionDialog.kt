package com.deslomator.tagtimer.ui.active.dialog

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.ui.ColorPicker
import com.deslomator.tagtimer.ui.DialogTextField
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
        mutableLongStateOf(event.elapsedTimeMillis)
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
        mutableIntStateOf(event.color)
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
        DialogTextField(
            value = label,
            onValueChange = { label = it },
            placeholder = R.string.label,
            icon = R.drawable.tag,
            enabled = enabled
        )
        DialogTextField(
            value = note,
            onValueChange = { note = it },
            placeholder = R.string.type_a_note,
            icon = R.drawable.note,
            enabled = enabled
        )
        DialogTextField(
            value = person,
            onValueChange = { person = it },
            placeholder = R.string.person,
            icon = R.drawable.person,
            enabled = enabled
        )
        DialogTextField(
            value = place,
            onValueChange = { place = it },
            placeholder = R.string.place,
            icon = R.drawable.place,
            enabled = enabled
        )
        Spacer(modifier = Modifier.height(7.dp))
        ColorPicker(
            selectedColor = Color(color),
            onItemClick = { color = it },
            enabled = enabled
        )
    }
}

private const val TAG = "EventEditionDialog"
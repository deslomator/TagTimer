package com.deslomator.tagtimer.ui.active.dialog

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.model.EventForDisplay
import com.deslomator.tagtimer.model.type.DialogState
import com.deslomator.tagtimer.model.type.LabelArchiveState
import com.deslomator.tagtimer.ui.ColorPicker
import com.deslomator.tagtimer.ui.DialogTextField
import com.deslomator.tagtimer.ui.MyDialog
import com.deslomator.tagtimer.ui.TimeNumberPicker
import com.deslomator.tagtimer.ui.theme.toHex
import com.deslomator.tagtimer.util.toColor

@Composable
fun EventEditionDialog(
    event4d: EventForDisplay,
    onAccept: (EventForDisplay) -> Unit,
    onDismiss: () -> Unit,
    enabled: Boolean = true,
) {
    var elapsed by rememberSaveable {
        mutableLongStateOf(event4d.event.elapsedTimeMillis)
    }
    var note by rememberSaveable {
        mutableStateOf(event4d.event.note)
    }
    var color by rememberSaveable {
        mutableStateOf(event4d.event.color)
    }
    // next: non editable fields
    var tag by rememberSaveable {
        mutableStateOf(event4d.tag?.name)
    }
    var person by rememberSaveable {
        mutableStateOf(event4d.person?.name)
    }
    var place by rememberSaveable {
        mutableStateOf(event4d.place?.name)
    }
    MyDialog(
        onDismiss = onDismiss,
        onAccept = {
            val e4d = event4d.copy(
                event = event4d.event.copy(
                    elapsedTimeMillis = elapsed,
                    note = note.trim(),
                    color = color
                )
            )
            onAccept(e4d)
        },
        dialogState = DialogState.EDIT_NO_DELETE,
        archiveState = LabelArchiveState.HIDDEN,
    ) {
        TimeNumberPicker(
            timeMillis = elapsed,
            onValueChange = { elapsed = it }
        )
        event4d.tag?.let { t ->
            DialogTextField(
                value = t.name,
                onValueChange = { tag = it },
                placeholder = R.string.tags,
                icon = R.drawable.tag,
                enabled = false
            )
        }
        DialogTextField(
            value = note,
            onValueChange = { note = it },
            placeholder = R.string.type_a_note,
            icon = R.drawable.note,
            enabled = enabled
        )
        event4d.person?.let { pr ->
            DialogTextField(
                value = pr.name,
                onValueChange = { person = it },
                placeholder = R.string.person,
                icon = R.drawable.person,
                enabled = false
            )
        }
        event4d.place?.let { p ->
            DialogTextField(
                value = p.name,
                onValueChange = { place = it },
                placeholder = R.string.place,
                icon = R.drawable.place,
                enabled = false
            )
        }
        Spacer(modifier = Modifier.height(7.dp))
        ColorPicker(
            selectedColor = color.toColor(),
            onItemClick = { color = it.toHex() },
            enabled = enabled
        )
    }
}

private const val TAG = "EventEditionDialog"
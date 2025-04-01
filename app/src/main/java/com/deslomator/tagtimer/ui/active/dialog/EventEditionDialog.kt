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
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.ancillary.EventForDisplay
import com.deslomator.tagtimer.model.type.DialogState
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
    activeTags: List<Label>? = null,
    activePersons: List<Label>? = null,
    activePlaces: List<Label>? = null,
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
    var tagId by rememberSaveable {
        mutableStateOf(event4d.tag?.id)
    }
    var personId by rememberSaveable {
        mutableStateOf(event4d.person?.id)
    }
    var placeId by rememberSaveable {
        mutableStateOf(event4d.place?.id)
    }

    MyDialog(
        onDismiss = onDismiss,
        onAccept = {
            val ev4d = event4d.copy(
                event = event4d.event.copy(
                    elapsedTimeMillis = elapsed,
                    note = note.trim(),
                    color = color,
                    tagId = tagId,
                    personId = personId,
                    placeId = placeId,
                )
            )
            onAccept(ev4d)
        },
        dialogState = DialogState.NEW_ITEM,
        onCopyClicked = { },
        onArchiveClicked = { },
        onUnArchiveClicked = { },
        onTrashClicked = { },
        onUnTrashClicked = { },
        onPurgeClicked = { },
    ) {
        TimeNumberPicker(
            timeMillis = elapsed,
            onValueChange = { elapsed = it },
            enabled = enabled
        )
        if (activeTags != null) {
            DialogComboBox(
                label = event4d.tag,
                onValueChange = { tagId = it.id },
                icon = R.drawable.tag,
                activeLabels = activeTags,
                noLabel = R.string.no_tag
            )
        } else {
            event4d.tag?.let { t ->
                DialogTextField(
                    value = t.name,
                    onValueChange = {},
                    placeholder = R.string.tags,
                    icon = R.drawable.tag,
                    enabled = false
                )
            }
        }
        DialogTextField(
            value = note,
            onValueChange = { note = it },
            placeholder = R.string.type_a_note,
            icon = R.drawable.note,
            enabled = enabled
        )
        if (activePersons != null) {
            DialogComboBox(
                label = event4d.person,
                onValueChange = { personId = it.id },
                icon = R.drawable.person,
                activeLabels = activePersons,
                noLabel = R.string.no_person
            )
        } else {
            event4d.person?.let { pr ->
                DialogTextField(
                    value = pr.name,
                    onValueChange = {},
                    placeholder = R.string.persons,
                    icon = R.drawable.person,
                    enabled = false
                )
            }
        }
        if (activePlaces != null) {
            DialogComboBox(
                label = event4d.place,
                onValueChange = { placeId = it.id },
                icon = R.drawable.place,
                activeLabels = activePlaces,
                noLabel = R.string.no_place
            )
        } else {
            event4d.place?.let { pl ->
                DialogTextField(
                    value = pl.name,
                    onValueChange = {},
                    placeholder = R.string.places,
                    icon = R.drawable.place,
                    enabled = false
                )
            }
        }
        Spacer(modifier = Modifier.height(7.dp))
        ColorPicker(
            selectedColor = color.toColor(),
            onItemClick = { color = it.toHex() },
            enabled = enabled,
        )
    }
}

private const val TAG = "EventEditionDialog"
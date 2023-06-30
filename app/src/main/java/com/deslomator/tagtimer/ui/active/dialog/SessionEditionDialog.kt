package com.deslomator.tagtimer.ui.active.dialog

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.ui.ColorPicker
import com.deslomator.tagtimer.ui.DialogTextField
import com.deslomator.tagtimer.ui.MyDialog

@Composable
fun SessionEditionDialog(
    session: Session,
    onAction: (ActiveSessionAction) -> Unit
) {
    var name by rememberSaveable {
        mutableStateOf(session.name)
    }
    var notes by rememberSaveable {
        mutableStateOf(session.notes)
    }
    var color by rememberSaveable {
        mutableStateOf(session.color)
    }
    MyDialog(
        onDismiss = { onAction(ActiveSessionAction.DismissSessionEditionDialog) },
        onAccept = {
            val s = session.copy(
                name = name,
                notes = notes,
                color = color
            )
            onAction(ActiveSessionAction.AcceptSessionEditionClicked(s))
        },
    ) {
        DialogTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = R.string.name,
            icon = R.drawable.document_and_ray
        )
        DialogTextField(
            value = notes,
            onValueChange = { notes = it },
            placeholder = R.string.type_a_note,
            icon = R.drawable.note
        )
        Spacer(modifier = Modifier.height(7.dp))
        ColorPicker(
            selectedColor = Color(color),
            onItemClick = { color = it }
        )
    }
}

private const val TAG = "SessionEditionDialog"
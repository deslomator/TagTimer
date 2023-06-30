package com.deslomator.tagtimer.ui.main.sessions

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
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
import com.deslomator.tagtimer.action.SessionsTabAction
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.ui.ColorPicker
import com.deslomator.tagtimer.ui.DialogTextField
import com.deslomator.tagtimer.ui.MyDialog

@Composable
fun SessionDialog(
    session: Session,
    onAction: (SessionsTabAction) -> Unit,
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
        onDismiss = {
            onAction(SessionsTabAction.DismissSessionDialog)
        },
        onAccept = {
            val s = session.copy(
                lastAccessMillis = System.currentTimeMillis(),
                name = name,
                notes = notes,
                color = color
            )
            onAction(SessionsTabAction.AcceptAddSessionClicked(s))
        },
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.new_session),
            textAlign = TextAlign.Center
        )
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
        ColorPicker(
            selectedColor = Color(color),
            onItemClick = { color = it }
        )
    }
}

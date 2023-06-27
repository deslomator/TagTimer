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
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.ui.ColorPicker
import com.deslomator.tagtimer.ui.MyDialog

@Composable
fun SessionEditionDialog(
    session: Session,
    onAction: (ActiveSessionAction) -> Unit
) {
    var name by rememberSaveable {
        mutableStateOf(session.name)
    }
    var color by rememberSaveable {
        mutableStateOf(session.color)
    }
    MyDialog(
        onDismiss = { onAction(ActiveSessionAction.DismissSessionEditionDialog) },
        onAccept = {
            val s = session.copy(
                name = name,
                color = color
            )
            onAction(ActiveSessionAction.AcceptSessionEditionClicked(s))
        },
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = { name = it },
            placeholder = { Text(text = stringResource(id = R.string.name)) },
        )
        ColorPicker(
            selectedColor = Color(color),
            onItemClick = { color = it }
        )
    }
}

private const val TAG = "SessionEditionDialog"
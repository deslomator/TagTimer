package com.deslomator.tagtimer.ui.main.sessions

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.SessionsTabAction
import com.deslomator.tagtimer.state.SessionsTabState
import com.deslomator.tagtimer.ui.ColorPicker
import com.deslomator.tagtimer.ui.MyDialog

@Composable
fun SessionDialog(
    state: SessionsTabState,
    onAction: (SessionsTabAction) -> Unit,
) {
    MyDialog(
        onDismiss = {
            onAction(SessionsTabAction.DismissSessionDialog)
        },
        onAccept = {
            onAction(SessionsTabAction.AcceptAddSessionClicked)
        },
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.new_session),
            textAlign = TextAlign.Center
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.sessionName,
            onValueChange = { onAction(SessionsTabAction.UpdateSessionName(it)) },
            placeholder = { Text(text = stringResource(id = R.string.name)) }
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.sessionNotes,
            onValueChange = { onAction(SessionsTabAction.UpdateSessionNotes(it)) },
            placeholder = { Text(text = stringResource(id = R.string.type_a_note)) }
        )
        ColorPicker(
            selectedColor = Color(state.sessionColor),
            onItemClick = { onAction(SessionsTabAction.UpdateSessionColor(it)) }
        )
    }
}

@Composable
@Preview
fun SessionDialogPreview() {
    SessionDialog(
        state = SessionsTabState(sessionColor = 0xff4477),
        onAction = {},
    )
}

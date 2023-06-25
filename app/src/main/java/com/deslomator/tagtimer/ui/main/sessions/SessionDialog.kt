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
import com.deslomator.tagtimer.action.SessionsScreenAction
import com.deslomator.tagtimer.state.SessionsScreenState
import com.deslomator.tagtimer.ui.ColorPicker
import com.deslomator.tagtimer.ui.MyDialog

@Composable
fun SessionDialog(
    state: SessionsScreenState,
    onAction: (SessionsScreenAction) -> Unit,
) {
    MyDialog(
        onDismiss = {
            onAction(SessionsScreenAction.DismissSessionDialog)
        },
        onAccept = {
            onAction(SessionsScreenAction.AcceptAddSessionClicked)
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
            onValueChange = { onAction(SessionsScreenAction.UpdateSessionName(it)) },
            placeholder = { Text(text = stringResource(id = R.string.name)) }
        )
        ColorPicker(
            selectedColor = Color(state.sessionColor),
            onItemClick = { onAction(SessionsScreenAction.UpdateSessionColor(it)) }
        )
    }
}

@Composable
@Preview
fun SessionDialogPreview() {
    SessionDialog(
        state = SessionsScreenState(sessionColor = 0xff4477),
        onAction = {},
    )
}

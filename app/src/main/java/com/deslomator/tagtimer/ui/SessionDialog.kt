package com.deslomator.tagtimer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.AppAction
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.state.AppState

@Composable
fun SessionDialog(
    state: AppState,
    onAction: (AppAction) -> Unit,
    session: Session
) {

    AlertDialog(
        modifier = Modifier.fillMaxWidth(.8f),
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = {
            onAction(AppAction.DismissSessionDialog)
        },
        title = {
            Text(
                text = stringResource(id = if(state.isEditingSession) R.string.edit_session
                else R.string.new_session)
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = state.sessionName,
                    onValueChange = { onAction(AppAction.UpdateSessionName(it)) },
                    placeholder = { Text(text = stringResource(id = R.string.name)) }
                )
                ColorPicker(
                    selectedColor = Color(state.sessionColor),
                    onItemClick = { onAction(AppAction.UpdateSessionColor(it)) }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onAction(AppAction.AcceptSessionEditionClicked(session))
                }
            ) {
                Text(stringResource(id = R.string.accept))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onAction(AppAction.DismissSessionDialog)
                }
            ) {
                Text(stringResource(id = R.string.cancel))
            }
        }
    )
}

@Composable
@Preview
fun SessionDialogPreview() {
    SessionDialog(
        state = AppState(sessionColor = 0xff4477),
        onAction = {},
        session = Session()
    )
}

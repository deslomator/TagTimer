package com.deslomator.tagtimer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.action.AppAction
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.state.AppState

@Composable
fun SessionDialog(
    state: AppState,
    onAction: (AppAction) -> Unit,
    modifier: Modifier = Modifier,
    session: Session
) {

    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onDismissRequest.
            onAction(AppAction.SessionEdited(session))
        },
        title = {
            Text(text = "Edit Session")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = state.sessionName,
                    onValueChange = { onAction(AppAction.UpdateSessionName(it)) },
                    placeholder = { Text(text = "Name")}
                )
                TextField(
                    value = state.sessionColor.toString(),
                    onValueChange = { onAction(AppAction.UpdateSessionColor(it.toLong())) },
                    placeholder = { Text(text = "Color")}
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onAction(AppAction.SessionEdited(session))
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onAction(AppAction.SessionEdited(session))
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

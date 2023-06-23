package com.deslomator.tagtimer.ui.active

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.ui.MyDialog

@Composable
fun SessionEditionDialog(
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit
) {
    MyDialog(
        onDismiss = { onAction(ActiveSessionAction.DismissSessionEditionDialog) }
    ) {
        Text("Session Edition Dialog")
    }
}

private const val TAG = "SessionEditionDialog"
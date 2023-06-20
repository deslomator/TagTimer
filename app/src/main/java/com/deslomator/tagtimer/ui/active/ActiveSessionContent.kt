package com.deslomator.tagtimer.ui.active

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.action.SessionsScreenAction
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.state.SessionsScreenState

@Composable
fun ActiveSessionContent(
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit
) {
    /*if (state.showSessionDialog) {
        SessionDialog(
            state = state,
            onAction = onAction,
            session = state.currentSession
        )
    }*/
    LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item { Text("Active Session") }
        }
}

private const val TAG = "ActiveSessionContent"
package com.deslomator.tagtimer.ui.active

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.state.ActiveSessionState

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
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(
                items = state.tags,
                key = { it.id }
            ) { item ->
                ListItem(
                    headlineContent = {
                        Text("Session ID: ${item.id}")
                    },
                    supportingContent = {
                        Text("Tag ID: ${item.id}")
                    },
                )
            }
        }
}

private const val TAG = "ActiveSessionContent"
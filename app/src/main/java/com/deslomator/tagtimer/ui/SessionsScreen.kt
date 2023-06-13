package com.deslomator.tagtimer.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.action.AppAction
import com.deslomator.tagtimer.state.AppState
import com.deslomator.tagtimer.ui.theme.PurpleGrey80

@Composable
fun SessionsScreen(
    state: AppState,
    onAction: (AppAction) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onAction(AppAction.AddSession) }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "add session"
                )
            }
        }
    ) { paddingValues ->
        if(state.isEditingSession) {
            SessionDialog(
                state = state,
                onAction = onAction,
                session = state.currentSession
            )
        }
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.sessions) { session ->
                Log.d("Sessjons Screen", "Loading item")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(session.color))
                ) {
                    Text(text = session.name)
                    Text(text = session.lastAccessMillis.toString())
                    IconButton(onClick = { onAction(AppAction.EditSession(session, 0)) }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit session"
                        )
                    }
                }
            }
        }
    }
}


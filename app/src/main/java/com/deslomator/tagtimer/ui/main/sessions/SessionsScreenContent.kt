package com.deslomator.tagtimer.ui.main.sessions

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.action.SessionsTabAction
import com.deslomator.tagtimer.model.type.DialogState
import com.deslomator.tagtimer.state.SessionsScreenState

@Composable
fun SessionsScreenContent(
    paddingValues: PaddingValues,
    outerNavHostController: NavHostController,
    state: SessionsScreenState,
    onAction: (SessionsTabAction) -> Unit,
) {
    BackHandler(enabled = state.sessionDialogState != DialogState.HIDDEN) {
        onAction(SessionsTabAction.DismissSessionDialog)
    }
    LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(6.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = state.sessions,
                key = { it.id!! }
            ) { session ->
                SessionListItem(session, outerNavHostController, onAction)
            }
        }
    }

private const val TAG = "SessionsScreenContent"
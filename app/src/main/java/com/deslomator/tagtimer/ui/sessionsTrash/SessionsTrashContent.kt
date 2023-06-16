package com.deslomator.tagtimer.ui.sessionsTrash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.action.SessionsTrashAction
import com.deslomator.tagtimer.state.SessionsTrashState
import com.deslomator.tagtimer.ui.MyListItem

@Composable
fun SessionsTrashContent(
    paddingValues: PaddingValues,
    state: SessionsTrashState,
    onAction: (SessionsTrashAction) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = state.sessions,
                key = { it.id }
            ) { session ->
                MyListItem(
                    session = session,
                    leadingIcon = Icons.Filled.ThumbUp,
                    onLeadingClick = { onAction(SessionsTrashAction.RestoreSessionClicked(session)) },
                    trailingIcon = Icons.Filled.Delete,
                    onTrailingClick = { onAction(SessionsTrashAction.DeleteSessionClicked(session)) },
                )
            }
        }
    }
}
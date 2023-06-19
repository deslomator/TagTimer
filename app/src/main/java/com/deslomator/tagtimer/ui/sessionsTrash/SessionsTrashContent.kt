package com.deslomator.tagtimer.ui.sessionsTrash

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.SessionsTrashAction
import com.deslomator.tagtimer.state.SessionsTrashState
import com.deslomator.tagtimer.toDateTime
import com.deslomator.tagtimer.ui.MyListItem
import com.deslomator.tagtimer.ui.theme.Pink80
import com.deslomator.tagtimer.ui.theme.SoftGreen

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
                    modifier = Modifier
                        .border(1.dp, Color.LightGray),
                    colors = ListItemDefaults.colors(
                        leadingIconColor = SoftGreen,
                        trailingIconColor = Pink80,
                        containerColor = Color(session.color).copy(alpha = .16f)
                    ),
                    item = session,
                    leadingIcon = R.drawable.baseline_restore_from_trash_24,
                    onLeadingClick = { onAction(SessionsTrashAction.RestoreSessionClicked(session)) },
                    trailingIcon = R.drawable.baseline_delete_forever_24,
                    onTrailingClick = { onAction(SessionsTrashAction.DeleteSessionClicked(session)) },
                    shadowElevation = 0.dp
                ) { item ->
                    Column {
                        Text(item.name)
                        Text(item.lastAccessMillis.toDateTime())
                    }
                }
            }
        }
    }
}
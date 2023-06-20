package com.deslomator.tagtimer.ui.active

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.state.ActiveSessionState

@Composable
fun TagSelectionDialog(
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit,
) {

    AlertDialog(
        modifier = Modifier.fillMaxWidth(.8f),
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = {
            onAction(ActiveSessionAction.DismissTagDialog)
        },
        title = {
            Text(
                text = stringResource(id = R.string.select_tags)
            )
        },
        text = {
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
                            Text("Tag label: ${item.label}")
                        },
                        supportingContent = {
                            Text("Tag ID: ${item.id}")
                        },
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onAction(ActiveSessionAction.AcceptTagSelectionClicked)
                }
            ) {
                Text(stringResource(id = R.string.accept))
            }
        },
    )
}

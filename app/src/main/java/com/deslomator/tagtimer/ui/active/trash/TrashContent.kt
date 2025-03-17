package com.deslomator.tagtimer.ui.active.trash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.TrashTabAction
import com.deslomator.tagtimer.state.TrashTabState
import com.deslomator.tagtimer.ui.active.EventListItem
import com.deslomator.tagtimer.ui.showSnackbar

@Composable
fun TrashContent(
    paddingValues: PaddingValues,
    state: TrashTabState,
    onAction: (TrashTabAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(6.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        if (state.trashedEvents.isEmpty()) {
            item {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(id = R.string.event_trash_is_empty)
                )
            }
        }
        items(
            items = state.trashedEvents,
            key = { it.event.id!! }
        ) { event4d ->
            EventListItem(
                event4d = event4d,
                leadingIcon = R.drawable.untrash,
                onLeadingClick = {
                    showSnackbar(
                        scope,
                        snackbarHostState,
                        context.getString(R.string.event_restored)
                    )
                    onAction(TrashTabAction.RestoreEventClicked(event4d))
                },
                trailingIcon = R.drawable.delete_forever,
                onTrailingClick = {
                    showSnackbar(
                        scope,
                        snackbarHostState,
                        context.getString(R.string.event_deleted)
                    )
                    onAction(TrashTabAction.DeleteEventClicked(event4d))
                },
                onItemClick = { onAction(TrashTabAction.EventInTrashClicked(event4d)) },
            )
        }
    }
}


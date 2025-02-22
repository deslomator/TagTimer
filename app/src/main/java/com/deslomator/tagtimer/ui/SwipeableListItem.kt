package com.deslomator.tagtimer.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue.EndToStart
import androidx.compose.material3.SwipeToDismissBoxValue.Settled
import androidx.compose.material3.SwipeToDismissBoxValue.StartToEnd
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.util.toDateTime

@Composable
fun SwipeableListItem(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when(it) {
                StartToEnd -> {
                    onDismiss()
                }
                EndToStart, Settled -> return@rememberSwipeToDismissBoxState false
            }
            return@rememberSwipeToDismissBoxState true
        },
        // positional threshold of 25%
        positionalThreshold = { it * .25f }
    )
    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromEndToStart = false,
        backgroundContent = { DismissBackground(dismissState)},
        content = {
            content()
        })
}

@Composable
@Preview
fun SwipeableListItemPreview() {
    SwipeableListItem(
        onDismiss = {},
    ) {
        MyListItem(
            item = Session(
                name = "Session in Gym",
                color = "FFE450D1",
                lastAccessMillis = 287539475785793
            ),
            onItemClick = {},
        ){ item ->
            Column {
                Text(item.name)
                Text(item.lastAccessMillis.toDateTime())
            }
        }
    }
}

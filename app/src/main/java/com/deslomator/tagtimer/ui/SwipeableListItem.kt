package com.deslomator.tagtimer.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissDirection.StartToEnd
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.toDateTime

/**
 * @param dismissDirection defines delete and done swipes.
 * [dismissState] is passed to content so it can use it
 * to change its appearance while dragging
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableListItem(
    dismissDirection: DismissDirection = StartToEnd,
    onDismiss: () -> Unit,
    dismissIcon: ImageVector = Icons.Default.Delete,
    doneIcon: ImageVector = Icons.Default.Done,
    dismissColor: Color = Color(0xFFEFB8C8),
    doneColor: Color = Color(0xff04a48f),
    content: @Composable (DismissState) -> Unit
) {
    val dismissState = rememberDismissState(initialValue = DismissValue.Default)
    if (dismissState.isDismissed(dismissDirection)) {
        onDismiss()
    }

    SwipeToDismiss(
        state = dismissState,
        modifier = Modifier.padding(vertical = 4.dp),
        directions = setOf(dismissDirection),
        background = {
            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissValue.Default -> Color.LightGray
                    DismissValue.DismissedToStart ->
                        if (dismissDirection == StartToEnd) doneColor
                        else dismissColor
                    DismissValue.DismissedToEnd ->
                        if (dismissDirection == StartToEnd) dismissColor
                        else doneColor
                }
            )
            val alignment = when (direction) {
                StartToEnd -> Alignment.CenterStart
                DismissDirection.EndToStart -> Alignment.CenterEnd
            }
            val icon = when (direction) {
                StartToEnd ->
                    if (dismissDirection == StartToEnd) dismissIcon
                    else doneIcon
                DismissDirection.EndToStart ->
                    if (dismissDirection == StartToEnd) doneIcon
                    else dismissIcon
            }
            val scale by animateFloatAsState(
                if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ) {
                Icon(
                    icon,
                    contentDescription = "Localized description",
                    modifier = Modifier.scale(scale)
                )
            }
        },
        dismissContent = { content(dismissState) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun SwipeableListItemPreview() {
    SwipeableListItem(
        onDismiss = {},
    ) {
        MyListItem(
            item = Session(
                name = "Session in Gym",
                color = -33929,
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

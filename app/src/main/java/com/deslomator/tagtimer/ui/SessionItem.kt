package com.deslomator.tagtimer.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.toDateTime

@Composable
fun SessionItem(
    session: Session,
    onItemClick: (Session) -> Unit,
    onEditClick: (Session) -> Unit,
    onDeleteClick: (Session) -> Unit,
) {
    ListItem(
        modifier = Modifier
            .clickable { onItemClick(session) },
        shadowElevation = 10.dp,
        headlineContent = { Text(session.name) },
        supportingContent = { Text(session.lastAccessMillis.toDateTime()) },
        trailingContent = {
            Row {
                IconButton(
                    onClick = { onEditClick(session) },
                ) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Edit",
                    )
                }
                IconButton(
                    onClick = { onDeleteClick(session) },
                ) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Delete",
                    )
                }
            }
        },
        leadingContent = {
            Image(
                painter = ColorPainter(Color(session.color)),
                contentDescription = "",
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Gray, CircleShape)
            )
        }
    )
}

@Composable
@Preview
fun SessionItemPreview() {
    SessionItem(
        session = Session(
            name = "Session in Gym",
            color = -33929,
            lastAccessMillis = 287539475785793
        ),
        onItemClick = {},
        onEditClick = {},
        onDeleteClick = {},
    )
}

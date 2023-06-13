package com.deslomator.tagtimer.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.model.Session

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
        supportingContent = { Text(session.lastAccessMillis.toString()) },
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
            Icon(
                Icons.Filled.CheckCircle,
                contentDescription = "Color",
                tint = Color(session.color)
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
            color = 0xc83929,
            lastAccessMillis = 287539475785793
        ),
        onItemClick = {},
        onEditClick = {},
        onDeleteClick = {},
    )
}

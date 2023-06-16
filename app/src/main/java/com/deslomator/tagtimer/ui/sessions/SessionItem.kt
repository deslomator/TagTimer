package com.deslomator.tagtimer.ui.sessions

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.toDateTime
import androidx.compose.runtime.getValue

@Composable
fun SessionItem(
    session: Session,
    onItemClick: (Session) -> Unit,
    onEditClick: (Session) -> Unit,
//    onDeleteClick: (Session) -> Unit,
    shadowElevation: Dp = 10.dp
) {
    val lastAccess by remember {
        derivedStateOf { session.lastAccessMillis.toDateTime() }
    }
    ListItem(
        modifier = Modifier
            .clickable { onItemClick(session) },
        shadowElevation = shadowElevation,
        headlineContent = { Text(session.name) },
        supportingContent = { Text(lastAccess) },
        trailingContent = {
//            Row {
            IconButton(
                onClick = { onEditClick(session) },
            ) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Edit",
                )
            }
            /*IconButton(
                onClick = { onDeleteClick(session) },
            ) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Delete",
                )
            }
        }*/
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
//        onDeleteClick = {},
    )
}

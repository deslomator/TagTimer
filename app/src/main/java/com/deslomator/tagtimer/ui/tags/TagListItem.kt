package com.deslomator.tagtimer.ui.tags

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.toDateTime

@Composable
fun TagListItem(
    session: Session,
    leadingIcon: Int? = null,
    onLeadingClick: ((Session) -> Unit)? = null,
    onItemClick: ((Session) -> Unit)? = null,
    trailingIcon: Int? = null,
    onTrailingClick: ((Session) -> Unit)? = null,
    shadowElevation: Dp = 10.dp
) {
    ListItem(
        modifier = Modifier
            .clickable { onItemClick?.invoke(session) },
        shadowElevation = shadowElevation,
        headlineContent = { Text(session.name) },
        leadingContent = {
            Row {
                leadingIcon?.let {
                    IconButton(
                        onClick = { onLeadingClick?.invoke(session) },
                    ) {
                        Icon(
                            painter = painterResource(id = it),
                            contentDescription = "Edit",
                        )
                    }
                }
                Image(
                    painter = ColorPainter(Color(session.color)),
                    contentDescription = "",
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Gray, CircleShape)
                )
            }
        },
        supportingContent = { Text(session.lastAccessMillis.toDateTime()) },
        trailingContent = {
            trailingIcon?.let {
                IconButton(
                    onClick = { onTrailingClick?.invoke(session) },
                ) {
                    Icon(
                        painter = painterResource(id = it),
                        contentDescription = "Edit",
                    )
                }
            }
        },
    )
}

@Composable
@Preview
fun TagListItemPreview() {
    TagListItem(
        session = Session(
            name = "Session in Gym",
            color = -33929,
            lastAccessMillis = 287539475785793
        ),
        onItemClick = {},
        onLeadingClick = {},
        onTrailingClick = {},
    )
}


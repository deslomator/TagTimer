package com.deslomator.tagtimer.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.toDateTime

@Composable
fun MyListItem(
    modifier: Modifier = Modifier,
    colors: ListItemColors = ListItemDefaults.colors(),
    session: Session,
    leadingIcon: Int? = null,
    onLeadingClick: ((Session) -> Unit)? = null,
    onItemClick: ((Session) -> Unit)? = null,
    trailingIcon: Int? = null,
    onTrailingClick: ((Session) -> Unit)? = null,
    shadowElevation: Dp = 10.dp
) {
    ListItem(
        modifier = modifier
            .clickable { onItemClick?.invoke(session) },
        colors = colors,
        shadowElevation = shadowElevation,
        headlineContent = { Text(session.name) },
        leadingContent = {
            Row {
                leadingIcon?.let {
                    IconButton(
                        onClick = { onLeadingClick?.invoke(session) },
                    ) {
                        Icon(
                            modifier = Modifier.size(36.dp),
                            painter = painterResource(id = it),
                            contentDescription = "Edit",
                        )
                    }
                }
            }
        },
        supportingContent = { Text(session.lastAccessMillis.toDateTime()) },
        trailingContent = {
            trailingIcon?.let {
                IconButton(
                    onClick = { onTrailingClick?.invoke(session) },
                ) {
                    Icon(
                        modifier = Modifier.size(36.dp),
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
fun MyListItemPreview() {
    MyListItem(
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


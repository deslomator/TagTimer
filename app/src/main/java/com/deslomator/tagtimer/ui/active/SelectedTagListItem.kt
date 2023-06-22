package com.deslomator.tagtimer.ui.active

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
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
fun <T> SelectedTagListItem(
    modifier: Modifier = Modifier,
    colors: ListItemColors = ListItemDefaults.colors(),
    item: T,
    leadingIcon: Int? = null,
    onLeadingClick: ((T) -> Unit)? = null,
    onItemClick: ((T) -> Unit)? = null,
    checked: Boolean = false,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    shadowElevation: Dp = 0.dp,
    headlineContent: @Composable (T) -> Unit
) {
    ListItem(
        modifier = modifier
            .clickable { onItemClick?.invoke(item) },
        colors = colors,
        shadowElevation = shadowElevation,
        headlineContent = { headlineContent(item) },
        leadingContent = {
            Row {
                leadingIcon?.let {
                    IconButton(
                        onClick = { onLeadingClick?.invoke(item) },
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
        trailingContent = {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        },
    )
}

@Composable
@Preview
fun SelectedTagListItemPreview() {
    SelectedTagListItem(
        item = Session(
            name = "Session in Gym",
            color = -33929,
            lastAccessMillis = 287539475785793
        ),
        onItemClick = {},
        onLeadingClick = {},
    ) { item ->
        Column {
            Text(item.name)
            Text(item.lastAccessMillis.toDateTime())
        }
    }
}

package com.deslomator.tagtimer.ui.tags

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.Tag

@Composable
fun TagListItem(
    modifier: Modifier = Modifier,
    colors: ListItemColors = ListItemDefaults.colors(),
    tag: Tag,
    leadingIcon: Int? = null,
    onLeadingClick: ((Tag) -> Unit)? = null,
    onItemClick: ((Tag) -> Unit)? = null,
    trailingIcon: Int? = null,
    onTrailingClick: ((Tag) -> Unit)? = null,
    shadowElevation: Dp = 10.dp
) {
    ListItem(
        modifier = modifier
            .clickable { onItemClick?.invoke(tag) },
        shadowElevation = shadowElevation,
        colors = colors,
        headlineContent = { Text(tag.label) },
        leadingContent = {
            Row {
                leadingIcon?.let {
                    IconButton(
                        onClick = { onLeadingClick?.invoke(tag) },
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
        supportingContent = { Text(tag.category) },
        trailingContent = {
            trailingIcon?.let {
                IconButton(
                    onClick = { onTrailingClick?.invoke(tag) },
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
fun TagListItemPreview() {
    TagListItem(
        tag = Tag(
            label = "Some Tag",
            color = -33929,
            category = " category 287539475785793"
        ),
        trailingIcon = R.drawable.baseline_edit_24,
        onItemClick = {},
        onLeadingClick = {},
        onTrailingClick = {},
    )
}


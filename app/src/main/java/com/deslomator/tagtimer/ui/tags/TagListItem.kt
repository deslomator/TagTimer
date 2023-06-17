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
import com.deslomator.tagtimer.model.Tag

@Composable
fun TagListItem(
    tag: Tag,
    leadingIcon: Int? = null,
    onLeadingClick: ((Tag) -> Unit)? = null,
    onItemClick: ((Tag) -> Unit)? = null,
    trailingIcon: Int? = null,
    onTrailingClick: ((Tag) -> Unit)? = null,
    shadowElevation: Dp = 10.dp
) {
    ListItem(
        modifier = Modifier
            .clickable { onItemClick?.invoke(tag) },
        shadowElevation = shadowElevation,
        headlineContent = { Text(tag.label) },
        leadingContent = {
            Row {
                leadingIcon?.let {
                    IconButton(
                        onClick = { onLeadingClick?.invoke(tag) },
                    ) {
                        Icon(
                            painter = painterResource(id = it),
                            contentDescription = "Edit",
                        )
                    }
                }
                Image(
                    painter = ColorPainter(Color(tag.color)),
                    contentDescription = "",
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Gray, CircleShape)
                )
            }
        },
        supportingContent = { Text(tag.category) },
        trailingContent = {
            trailingIcon?.let {
                IconButton(
                    onClick = { onTrailingClick?.invoke(tag) },
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
        tag = Tag(
            label = "Session in Gym",
            color = -33929,
            category = " category287539475785793"
        ),
        onItemClick = {},
        onLeadingClick = {},
        onTrailingClick = {},
    )
}


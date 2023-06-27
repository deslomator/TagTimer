package com.deslomator.tagtimer.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.toDateTime

@Composable
fun <T> MyListItem(
    modifier: Modifier = Modifier,
    colors: CardColors = CardDefaults.cardColors(),
    shape: Shape = RectangleShape,
    border: BorderStroke? = null,
    item: T,
    @DrawableRes leadingIcon: Int? = null,
    onLeadingClick: ((T) -> Unit)? = null,
    onItemClick: ((T) -> Unit)? = null,
    @DrawableRes trailingIcon: Int? = null,
    onTrailingClick: ((T) -> Unit)? = null,
    centralContent: @Composable RowScope.(T) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick?.invoke(item) },
        shape = shape,
        colors = colors,
        border = border,
    ) {
        Row(
            modifier = modifier
            .padding(start = 20.dp, end = 8.dp, top = 10.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
            Row(modifier = Modifier.weight(1.0f)) {
                centralContent(item)
            }
            trailingIcon?.let {
                IconButton(
                    onClick = { onTrailingClick?.invoke(item) },
                ) {
                    Icon(
                        modifier = Modifier.size(36.dp),
                        painter = painterResource(id = it),
                        contentDescription = "Edit",
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun MyListItemPreview() {
    MyListItem(
        item = Session(
            name = "Session in Gym",
            color = -33929,
            lastAccessMillis = 287539475785793
        ),
        onItemClick = {},
        onLeadingClick = {},
        onTrailingClick = {},
    ) { item ->
        Column {
            Text(item.name)
            Text(item.lastAccessMillis.toDateTime())
        }
    }
}


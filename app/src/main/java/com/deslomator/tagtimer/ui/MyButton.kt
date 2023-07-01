package com.deslomator.tagtimer.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun <T>MyButton(
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    border: BorderStroke? = null,
    item: T,
    text: String,
    iconSize: Dp = 26.dp,
    @DrawableRes leadingIcon: Int? = null,
    onLeadingClick: ((T) -> Unit)? = null,
    onItemClick: ((T) -> Unit)? = null,
    @DrawableRes trailingIcon: Int? = null,
    onTrailingClick: ((T) -> Unit)? = null,
) {
    Button(
        modifier = modifier,
        onClick = { onItemClick?.invoke(item) },
        colors = colors,
        border = border
    ) {
        leadingIcon?.let {
            IconButton(
                modifier = Modifier.size(iconSize + 2.dp),
                onClick = { onLeadingClick?.invoke(item) },
            ) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    painter = painterResource(id = it),
                    contentDescription = "Edit",
                )
            }
        }
        Row(
            modifier = Modifier.weight(1.0f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1F),
                text = text,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Clip
            )
        }
        trailingIcon?.let {
            IconButton(
                modifier = Modifier.size(iconSize + 2.dp),
                onClick = { onTrailingClick?.invoke(item) },
            ) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    painter = painterResource(id = it),
                    contentDescription = "Edit",
                )
            }
        }
    }
}
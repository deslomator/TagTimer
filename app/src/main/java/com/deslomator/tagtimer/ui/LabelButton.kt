package com.deslomator.tagtimer.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.type.Checked
import com.deslomator.tagtimer.ui.theme.VeryLightGray
import com.deslomator.tagtimer.ui.theme.brightness
import com.deslomator.tagtimer.ui.theme.contrasted

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LabelButton(
    modifier: Modifier = Modifier,
    item: Label,
    isTrash: Boolean = false,
    iconSize: Dp = 26.dp,
    onLeadingClick: ((Label) -> Unit)? = null,
    onItemClick: ((Label) -> Unit)? = null,
    onLongClick: ((Label) -> Unit)? = null,
    onTrailingClick: ((Label) -> Unit)? = null,
    checked: Boolean = true,
    checkType: Checked = Checked.NONE
) {
    val borderWidth = if (item is Label.Person) 5.dp else 1.dp
    val borderColor = if (item is Label.Person) {
        if (Color(item.color).brightness() > .9F) VeryLightGray
        else Color(item.color)
    } else {
        Color.LightGray
    }
    val containerColor =
        if (item is Label.Person) Color.White
        else Color(item.color)
    val contentColor = containerColor.contrasted()
    val icon = when (item) {
        is Label.Tag -> painterResource(id = R.drawable.tag)
        is Label.Place -> painterResource(id = R.drawable.place)
        is Label.Person -> painterResource(id = R.drawable.person)
    }
    val leadingIcon = if (isTrash) painterResource(id = R.drawable.restore_from_trash) else icon
    val trailingIcon = if (isTrash) painterResource(id = R.drawable.delete_forever) else null
    val iconPadding = if (isTrash) 9.dp else 0.dp
    val containerPadding by animateDpAsState(
        targetValue = if (checked && checkType == Checked.LEADING) 12.dp else 5.dp
    )
    Box {
        Row(
            modifier = modifier
                .then(onItemClick?.let {
                    Modifier.combinedClickable(
                        onClick = { onItemClick(item) },
                        onLongClick = { onLongClick?.invoke(item) }
                    )
                } ?: Modifier)
                .clip(RoundedCornerShape(50))
                .background(containerColor)
                .border(borderWidth, borderColor, RoundedCornerShape(50))
                .padding(start = 5.dp, end = 5.dp)
                .padding(top = containerPadding, bottom = containerPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .then(onLeadingClick?.let {
                        Modifier.clickable(onClick = { it(item) })
                    } ?: Modifier)
                    .padding(iconPadding)
                    .size(iconSize),
                painter = leadingIcon,
                contentDescription = "restore",
                tint = contentColor
            )
            Text(
                modifier = Modifier.weight(1F),
                color = contentColor,
                text = item.name,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Clip
            )
            trailingIcon?.let {
                Icon(
                    modifier = Modifier
                        .clickable(
                            onClick = { onTrailingClick?.invoke(item) }
                        )
                        .padding(iconPadding)
                        .size(iconSize),
                    painter = it,
                    contentDescription = "delete forever",
                    tint = contentColor
                )
            }
            AnimatedVisibility(visible = checked && checkType == Checked.TRAILING) {
                Icon(
                    modifier = Modifier
                        .size(iconSize),
                    painter = painterResource(id = R.drawable.check),
                    contentDescription = "checked",
                    tint = contentColor
                )
            }
        }
    }
}

    private const val TAG = "LabelButton"
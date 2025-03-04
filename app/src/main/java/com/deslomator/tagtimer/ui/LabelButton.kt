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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
    label: Label,
    isTrash: Boolean = false,
    iconSize: Dp = 26.dp,
    onLeadingClick: ((Label) -> Unit)? = null,
    onItemClick: ((Label) -> Unit)? = null,
    onLongClick: ((Label) -> Unit)? = null,
    onTrailingClick: ((Label) -> Unit)? = null,
    checked: Boolean = true,
    checkType: Checked = Checked.NONE,
    archived: Boolean = false,
) {
    val borderWidth =  if (label.isPerson()) 5.dp else 1.dp
    val borderColor = if (label.isPerson()) {
        if (Color(label.longColor).brightness() > .9F) VeryLightGray
        else Color(label.longColor)
    } else {
        Color.LightGray
    }
    val secondaryContainer = MaterialTheme.colorScheme.tertiaryContainer
    val containerColor by remember(label.color) {
        derivedStateOf {
            if (label.isPerson()) secondaryContainer
            else Color(label.longColor)
        }
    }
    val secondary = MaterialTheme.colorScheme.tertiary
    val contentColor by remember(label.color) {
        derivedStateOf {
            if (label.isPerson()) secondary
            else containerColor.contrasted()
        }
    }
    val leadingIcon = if (isTrash) {
        R.drawable.restore_from_trash
    } else {
        label.getIcon()
    }
    val trailingIcon = when {
        isTrash -> R.drawable.delete_forever
        archived -> R.drawable.folder
        else -> null
    }
    val iconPadding = if (isTrash) 9.dp else 0.dp
    val containerPadding by animateDpAsState(
        targetValue = if (checked && checkType == Checked.LEADING) 12.dp else 5.dp
    )
    Box {
        Row(
            modifier = modifier
                .then(onItemClick?.let {
                    Modifier.combinedClickable(
                        onClick = { onItemClick(label) },
                        onLongClick = { onLongClick?.invoke(label) }
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
                        Modifier.clickable(onClick = { it(label) })
                    } ?: Modifier)
                    .padding(iconPadding)
                    .size(iconSize),
                painter = painterResource(id = leadingIcon),
                contentDescription = "restore",
                tint = contentColor
            )
            Text(
                modifier = Modifier.weight(1F),
                color = contentColor,
                text = label.name,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Clip
            )
            trailingIcon?.let {
                Icon(
                    modifier = Modifier
                        .clickable(
                            onClick = { onTrailingClick?.invoke(label) }
                        )
                        .padding(iconPadding)
                        .size(iconSize),
                    painter = painterResource(id = it),
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
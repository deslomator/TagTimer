package com.deslomator.tagtimer.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.model.Lbl
import com.deslomator.tagtimer.ui.theme.VeryLightGray
import com.deslomator.tagtimer.ui.theme.brightness
import com.deslomator.tagtimer.ui.theme.contrasted

@Composable
fun LabelButton(
    modifier: Modifier = Modifier,
    item: Lbl,
    isTrash: Boolean = false,
    iconSize: Dp = 26.dp,
    onLeadingClick: ((Lbl) -> Unit)? = null,
    onItemClick: ((Lbl) -> Unit)? = null,
    onTrailingClick: ((Lbl) -> Unit)? = null,
    checked: Boolean
) {
    val borderWidth = if (item is Lbl.Person) 5.dp else 1.dp
    val borderColor = if (item is Lbl.Person) {
        if (Color(item.color).brightness() > .85F) VeryLightGray
        else Color(item.color)
    } else {
        Color.LightGray
    }
    val containerColor = if (item is Lbl.Person) Color.White else Color(item.color)
    val colors = ButtonDefaults.buttonColors(
        containerColor = containerColor,
        contentColor = containerColor.contrasted()
    )
    val icon = when (item) {
        is Lbl.Tag -> R.drawable.tag
        is Lbl.Place -> R.drawable.place
        is Lbl.Person -> R.drawable.person
    }
    val leadingIcon = if (isTrash) R.drawable.restore_from_trash else icon
    val trailingIcon = if (isTrash) R.drawable.delete_forever else null
    val alpha = if (checked) 1F else .4F
    Button(
        modifier = modifier.alpha(alpha),
        onClick = { onItemClick?.invoke(item) },
        colors = colors,
        border = BorderStroke(borderWidth, borderColor)
    ) {
        IconButton(
            modifier = Modifier.size(iconSize + 2.dp),
            onClick = {
                if (isTrash) onLeadingClick?.invoke(item)
                else onItemClick?.invoke(item)
            },
        ) {
            Icon(
                modifier = Modifier.size(iconSize),
                painter = painterResource(id = leadingIcon),
                contentDescription = "restore",
            )
        }
        Text(
            modifier = Modifier.weight(1F),
            text = item.name,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Clip
        )
        trailingIcon?.let {
            IconButton(
                modifier = Modifier.size(iconSize + 2.dp),
                onClick = { onTrailingClick?.invoke(item) },
            ) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    painter = painterResource(id = it),
                    contentDescription = "delete forever",
                )
            }
        }
    }
}
package com.deslomator.tagtimer.ui.active

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.type.Checked
import com.deslomator.tagtimer.model.type.ItemState
import com.deslomator.tagtimer.model.type.LabelType
import com.deslomator.tagtimer.ui.theme.VeryLightGray
import com.deslomator.tagtimer.ui.theme.brightness
import com.deslomator.tagtimer.ui.theme.colorPickerColors
import com.deslomator.tagtimer.ui.theme.contrasted
import com.deslomator.tagtimer.ui.theme.toHex

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LabelButton(
    modifier: Modifier = Modifier,
    label: Label,
    iconSize: Dp = 26.dp,
    onLeadingClick: ((Label) -> Unit)? = null,
    onItemClick: ((Label) -> Unit)? = null,
    onLongClick: ((Label) -> Unit)? = null,
    checked: Boolean = true,
    checkType: Checked = Checked.NONE,
    square: Boolean = false
) {
    val borderWidth = if (label.isPerson()) 5.dp else 1.dp
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
    val checkSize by animateDpAsState(
        targetValue = if (checkType == Checked.SIZE && checked) 90.dp else 70.dp
    )
    Column {
        AnimatedVisibility(
            visible = square && checkType == Checked.SIZE && checked
        ) {
            Box(
                modifier = Modifier
                    .width(70.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(50))
                    .background(containerColor)
                    .border(borderWidth, borderColor, RoundedCornerShape(50))
            )
            /*Icon(
                painter = painterResource(R.drawable.expand_less),
                tint = contentColor,
                contentDescription = "checked",
            )*/
        }
        Box(
            modifier = modifier
                .then(onItemClick?.let {
                    Modifier.combinedClickable(
                        onClick = { onItemClick(label) },
                        onLongClick = { onLongClick?.invoke(label) }
                    )
                } ?: Modifier)
                .then(
                    if (square) Modifier
//                        .width(checkSize)
                        .width(70.dp)
                        .aspectRatio(1F) else Modifier
                )
                .clip(RoundedCornerShape(if (square) 20 else 50))
                .background(containerColor)
                .border(borderWidth, borderColor, RoundedCornerShape(if (square) 20 else 50)),
            contentAlignment = if (square) Alignment.Center else Alignment.CenterEnd
        ) {

            Row(
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp)
                    .padding(top = 5.dp, bottom = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!square) {
                    Icon(
                        modifier = Modifier
                            .then(onLeadingClick?.let {
                                Modifier.clickable(onClick = { it(label) })
                            } ?: Modifier)
                            .size(iconSize),
                        painter = painterResource(id = label.getIcon()),
                        contentDescription = "restore",
                        tint = contentColor
                    )
                }
                Text(
                    modifier = Modifier.weight(1F),
                    color = contentColor,
                    fontSize = if (square) 20.sp else TextUnit.Unspecified,
                    text = label.name,
                    textAlign = TextAlign.Center,
                    maxLines = if (square) 2 else 1,
                    overflow = TextOverflow.Clip
                )
                if (!square) {
                    when (val s = label.state) {
                        ItemState.ENABLED -> {}
                        ItemState.ARCHIVED -> {
                            Icon(
                                modifier = Modifier.size(iconSize),
                                painter = painterResource(id = s.iconId),
                                contentDescription = "delete forever",
                                tint = contentColor
                            )
                        }

                        ItemState.TRASHED -> {
                            Icon(
                                modifier = Modifier.size(iconSize),
                                painter = painterResource(id = s.iconId),
                                contentDescription = "delete forever",
                                tint = contentColor
                            )
                        }
                    }
                }

                AnimatedVisibility(visible = checked && checkType == Checked.TRAILING) {
                    Icon(
                        modifier = Modifier
                            .size(iconSize)
                            .padding(end = 4.dp),
                        painter = painterResource(id = R.drawable.check),
                        contentDescription = "checked",
                        tint = contentColor
                    )
                }
            }
        }
    }
}

private const val TAG = "LabelButton"

@Composable
@Preview
fun LabelButtonPreview() {
    Column {
        LabelButton(
            label = Label(
                name = "my label",
                type = LabelType.TAG,
                color = colorPickerColors[12].toHex()
            ),
            checked = true,
            checkType = Checked.TRAILING
        )
        LabelButton(
            label = Label(
                name = "my label",
                type = LabelType.PERSON,
                color = colorPickerColors[26].toHex()
            )
        )
        LabelButton(
            label = Label(
                name = "my label",
                type = LabelType.PLACE,
                color = colorPickerColors[22].toHex()
            ),
            square = true
        )
    }
}
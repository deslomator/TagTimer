package com.deslomator.tagtimer.ui.active

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.toElapsedTime
import com.deslomator.tagtimer.ui.MyListItem
import com.deslomator.tagtimer.ui.theme.OnDarkBackground
import com.deslomator.tagtimer.ui.theme.OnLightBackground
import com.deslomator.tagtimer.ui.theme.brightness

@Composable
fun EventListItem(
    event: Event,
    leadingIcon: Int? = null,
    onLeadingClick: ((Event) -> Unit)? = null,
    trailingIcon: Int? = null,
    onTrailingClick: ((Event) -> Unit)? = null,
    onItemClick: () -> Unit,
    persons: List<String> = emptyList()
) {
    val borderColor =
        if (Color(event.color).brightness() > 0.9f) OnDarkBackground.toArgb()
        else event.color
    val index by remember {
        derivedStateOf {
            maxOf(persons.indexOf(event.person), 0)
        }
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        SubcomposeLayout { constraints ->
            val item = subcompose(1) {
                MyListItem(
                    modifier = Modifier.width(190.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                        contentColor = OnLightBackground,
                    ),
                    shape = RoundedCornerShape(15.dp),
                    border = BorderStroke(4.dp, Color(borderColor)),
                    item = event,
                    onItemClick = { onItemClick() },
                    iconSize = 22.dp,
                    leadingIcon = leadingIcon,
                    onLeadingClick = onLeadingClick,
                    trailingIcon = trailingIcon,
                    onTrailingClick = onTrailingClick
                ) { item ->
                    Text(
                        text = listOf(item.tag, item.place,item.person)
                            .filter { it.isNotEmpty() }.joinToString(separator = ","),
                        maxLines = 1,
                        overflow = TextOverflow.Clip
                    )
                }
            }.map { it.measure(constraints) }.first()

            val time = subcompose(2) {
                Text(
                    modifier = Modifier
                        .padding(start = 7.dp)
                        .width(70.dp),
                    text = event.elapsedTimeMillis.toElapsedTime(),
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
            }.map { it.measure(constraints) }.first()

            layout(
                width = constraints.maxWidth,
                height = item.height
            ) {
                val freeWidth = constraints.maxWidth - item.width - time.width
                val step = if (persons.size < 2) 0 else freeWidth / (persons.size - 1)
                val xOffset = index * step
                val yOffset = (item.height - time.height) / 2

                item.place(x = xOffset, y = 0)
                time.place(x = constraints.maxWidth - time.width, y = yOffset)
            }
        }

    }
}

@Composable
@Preview
fun EventListItemPreview() {
    val event = Event(
        tag = "label",
        note = "fff",
        elapsedTimeMillis = 25_000L,
    )
    val event2 = Event(
        tag = "label",
        note = "",
        elapsedTimeMillis = 25_000L,
    )
    Column {
        EventListItem(
            trailingIcon = if (event.note.isEmpty()) null else R.drawable.note,
            event = event,
            onItemClick = { },
        )
        EventListItem(
            trailingIcon = if (event2.note.isEmpty()) null else R.drawable.note,
            event = event2,
            onItemClick = { },
        )
    }
}

private const val TAG = "EventListItem"
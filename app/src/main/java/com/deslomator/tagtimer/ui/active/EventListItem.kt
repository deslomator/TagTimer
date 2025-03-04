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
import com.deslomator.tagtimer.model.EventForDisplay
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.util.toElapsedTime
import com.deslomator.tagtimer.ui.MyListItem
import com.deslomator.tagtimer.ui.theme.OnDarkBackground
import com.deslomator.tagtimer.ui.theme.OnLightBackground
import com.deslomator.tagtimer.ui.theme.brightness

@Composable
fun EventListItem(
    event4d: EventForDisplay,
    leadingIcon: Int? = null,
    onLeadingClick: ((EventForDisplay) -> Unit)? = null,
    trailingIcon: Int? = null,
    onTrailingClick: ((EventForDisplay) -> Unit)? = null,
    onItemClick: () -> Unit,
    persons: List<String> = emptyList()
) {
//    Log.d(TAG, "recomposing event, id: ${event.id}")
    val borderColor =
        if (Color(event4d.event.longColor).brightness() > 0.9f) OnDarkBackground.toArgb().toLong()
        else event4d.event.longColor
    val index by remember {
        derivedStateOf {
            maxOf(persons.indexOf(event4d.person?.name), 0)
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
                    item = event4d,
                    onItemClick = { onItemClick() },
                    iconSize = 22.dp,
                    leadingIcon = leadingIcon,
                    onLeadingClick = onLeadingClick,
                    trailingIcon = trailingIcon,
                    onTrailingClick = onTrailingClick
                ) { item ->
                    Text(
                        text = listOf(item.tag?.name, item.place?.name,item.person?.name)
                            .filter { !it.isNullOrEmpty() }.joinToString(separator = ","),
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
                    text = event4d.event.elapsedTimeMillis.toElapsedTime(),
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
    val event4d = EventForDisplay(
        event = Event(
        note = "fff",
        elapsedTimeMillis = 25_000L,),
        Label(name = "tag 1"),
        Label(name = "person 1"),
        Label(name = "place 1"),
    )
    val event2 = EventForDisplay(
        event = Event(
            note = "",
            elapsedTimeMillis = 25_000L,),
        Label(name = "tag 2"),
        Label(name = "person 1"),
        Label(name = "place 4"),
    )
    Column {
        EventListItem(
            trailingIcon = if (event4d.event.note.isEmpty()) null else R.drawable.note,
            event4d = event4d,
            onItemClick = { },
        )
        EventListItem(
            trailingIcon = if (event2.event.note.isEmpty()) null else R.drawable.note,
            event4d = event2,
            onItemClick = { },
        )
    }
}

private const val TAG = "EventListItem"
package com.deslomator.tagtimer.ui.active

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Person
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
    persons: List<Person> = emptyList()
) {
    val borderColor =
        if (Color(event.color).brightness() > 0.9f) OnDarkBackground.toArgb()
        else event.color
    val offset by remember {
        derivedStateOf {
            val names = persons.map { it.name }
            val o = if (names.contains(event.person)) {
                (-50).dp * names.indexOf(event.person)
            } else 0.dp
            o
        }
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Row(
            modifier = Modifier
                .offset(x = offset)
                .width(170.dp),
        ) {
            MyListItem(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                    contentColor = OnLightBackground,
                ),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(4.dp, Color(borderColor)),
                item = event,
                onItemClick = { onItemClick() },
                leadingIcon = leadingIcon,
                onLeadingClick = onLeadingClick,
                trailingIcon = trailingIcon,
                onTrailingClick = onTrailingClick
            ) { item ->
                Text(
                    text = "${item.label}  ${item.person}  ${item.place}",
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
//            modifier = Modifier.weight(.2f),
            text = event.elapsedTimeMillis.toElapsedTime()
        )
    }
}

@Composable
@Preview
fun EventListItemPreview() {
    val event = Event(
        label = "label",
        category = "category",
        note = "",
        elapsedTimeMillis = 25_000L,
    )
    EventListItem(
        trailingIcon = if (event.note.isEmpty()) null else R.drawable.note,
        event = event,
        onItemClick = {  },
        )
}
@Composable
@Preview
fun EventListItemPreview2() {
    val event = Event(
        label = "label",
        category = "category",
        note = "this is a note",
        elapsedTimeMillis = 25_000L,
    )
    EventListItem(
        trailingIcon = if (event.note.isEmpty()) null else R.drawable.edit_note,
        event = event,
        onItemClick = {  },
        )
}

private const val TAG = "EventListItem"
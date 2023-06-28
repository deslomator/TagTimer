package com.deslomator.tagtimer.ui.active

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
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
) {
    val borderColor =
        if (Color(event.color).brightness() > 0.9f) OnDarkBackground.toArgb()
        else event.color
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(0.8f),
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
                Text(item.label)
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            modifier = Modifier.weight(.2f),
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
        trailingIcon = if (event.note.isEmpty()) null else R.drawable.edit_note,
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
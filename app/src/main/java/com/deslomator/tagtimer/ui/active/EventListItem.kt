package com.deslomator.tagtimer.ui.active

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
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
        MyListItem(
            modifier = Modifier
                .weight(.8f)
                .clip(shape = RoundedCornerShape(20.dp))
                .border(4.dp, Color(borderColor), RoundedCornerShape(20.dp)),
            colors = ListItemDefaults.colors(
                headlineColor = OnLightBackground,
            ),
            item = event,
            onItemClick = { onItemClick() },
            leadingIcon = leadingIcon,
            onLeadingClick = onLeadingClick,
            trailingIcon = trailingIcon,
            onTrailingClick = onTrailingClick
        ) { item ->
            Text(item.label)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            modifier = Modifier.weight(.2f),
            text = event.elapsedTimeMillis.toElapsedTime()
        )
    }
}

private const val TAG = "EventListItem"
package com.deslomator.tagtimer.ui.active

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.model.Event
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
    noteEnabled: Boolean = true,
    onAcceptEventNote: ((String) -> Unit)? = null,
) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }
    val borderColor =
        if (Color(event.color).brightness() > 0.9f) OnDarkBackground.toArgb()
        else event.color
    MyListItem(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(20.dp))
            .border(4.dp, Color(borderColor), RoundedCornerShape(20.dp)),
        colors = ListItemDefaults.colors(
            headlineColor = OnLightBackground,
        ),
        item = event,
        onItemClick = { expanded = true },
        leadingIcon = leadingIcon,
        onLeadingClick = onLeadingClick,
        trailingIcon = trailingIcon,
        onTrailingClick = onTrailingClick
    ) { item ->
        Column {
            Text(item.label + "   " + item.category + "   " + item.note)
        }
        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            var note by rememberSaveable {
                mutableStateOf(event.note)
            }
            Column {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = note,
                    onValueChange = { note = it },
                    placeholder = {
                        Text(
                            text = if (noteEnabled) stringResource(id = R.string.type_a_note) else ""
                        )
                    },
                    enabled = noteEnabled
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            expanded = false
                            onAcceptEventNote?.invoke(note)
                            Log.d(TAG, "onClick() note: $note, id: ${event.id}, category: ${event.category}, label: ${event.label}")
                        }
                    ) {
                        Text(text = stringResource(id = R.string.accept))
                    }
                }
            }
        }
    }
}

private const val TAG = "EventListItem"
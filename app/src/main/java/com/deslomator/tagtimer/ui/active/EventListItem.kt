package com.deslomator.tagtimer.ui.active

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.ui.MyListItem
import com.deslomator.tagtimer.ui.theme.OnLightBackground

@Composable
fun EventListItem(
    event: Event,
    onAction: (ActiveSessionAction) -> Unit
) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }
    var note by rememberSaveable {
        mutableStateOf("")
    }
    MyListItem(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(20.dp))
            .border(4.dp, Color(event.color), RoundedCornerShape(20.dp)),
        colors = ListItemDefaults.colors(
            headlineColor = OnLightBackground,
        ),
        item = event,
        onItemClick = {
            note = event.note
            expanded = true
        },
    ) { item ->
        Column {
            Text(item.label + "   " + item.timestampMillis)
        }
        if (expanded) {
            TextField(
                value = note,
                onValueChange = { note = it },
                placeholder = { Text(text = stringResource(id = R.string.type_a_note)) },
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        expanded = false
                        onAction(ActiveSessionAction.AcceptEventNoteChanged(item, note))
                    }
                ) {
                    Text(text = stringResource(id = R.string.accept))
                }
            }
        }
    }
}
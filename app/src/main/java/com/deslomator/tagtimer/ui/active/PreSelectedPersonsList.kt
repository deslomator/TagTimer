package com.deslomator.tagtimer.ui.active

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.type.Checked
import com.deslomator.tagtimer.ui.LabelButton

@Composable
fun PreSelectedPersonsList(
    persons: List<Label.Person>,
    currentPerson: String,
    onItemClick: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier.padding(start = 5.dp, end = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(
            items = persons,
            key = { it.id }
        ) { person ->
            val checked by remember(currentPerson) {
                derivedStateOf { currentPerson == person.name }
            }
//            Log.d(TAG, "recomposing PersonButton, id: ${person.id}")
            LabelButton(
                modifier = Modifier.width(100.dp),
                item = person,
                onItemClick = { onItemClick(person.name) },
                checked = checked,
                checkType = Checked.LEADING,
            )
        }
    }
}

private const val TAG = "PreSelectedPersonsList"
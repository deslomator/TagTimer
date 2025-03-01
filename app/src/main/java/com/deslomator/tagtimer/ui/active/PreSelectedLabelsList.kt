package com.deslomator.tagtimer.ui.active

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
fun PreSelectedLabelsList(
    labels: List<Label>,
    currentLabel: Label?,
    onItemClick: (Label) -> Unit
) {
    if (labels.size in 1..3) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            labels.forEachIndexed { index, label ->
                val checked by remember(currentLabel) {
                    derivedStateOf { currentLabel == label }
                }
                LabelButton(
                    modifier = Modifier.fillMaxWidth(1F / (labels.size -index)),
                    item = label,
                    onItemClick = { onItemClick(label) },
                    checked = checked,
                    checkType = Checked.LEADING,
                )
            }
        }
    } else if (labels.size > 3) {
        LazyRow(
            modifier = Modifier.padding(start = 5.dp, end = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(
                items = labels,
                key = { it.id!! }
            ) { label ->
                val checked by remember(currentLabel) {
                    derivedStateOf { currentLabel == label }
                }
//            Log.d(TAG, "recomposing PersonButton, id: ${person.id}")
                LabelButton(
                    modifier = Modifier.width(120.dp),
                    item = label,
                    onItemClick = { onItemClick(label) },
                    checked = checked,
                    checkType = Checked.LEADING,
                )
            }
        }
    }
}

private const val TAG = "PreSelectedPersonsList"

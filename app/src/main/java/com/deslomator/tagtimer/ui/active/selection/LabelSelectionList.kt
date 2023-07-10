package com.deslomator.tagtimer.ui.active.selection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Preselected
import com.deslomator.tagtimer.model.type.Checked
import com.deslomator.tagtimer.ui.LabelButton

@Composable
fun LabelSelectionList(
    labels: List<Label>,
    preSelected: List<Preselected>,
    onCheckedChange: (Long, Boolean) -> Unit,
    onLongClick: (Label) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(6.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        columns = GridCells.Adaptive(minSize = 150.dp)
    ) {
        items(
            items = labels,
            key = { it.id }
        ) { label ->
            val checked by remember(preSelected) {
                derivedStateOf { preSelected.map { it.labelId }.contains(label.id) }
            }
            LabelButton(
                item = label,
                onItemClick = {
                    onCheckedChange(label.id, !checked)
                },
                onLongClick = { onLongClick(label) },
                checked = checked,
                checkType = Checked.TRAILING,
            )
        }
    }
}

private const val TAG = "LabelSelectionList"
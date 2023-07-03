package com.deslomator.tagtimer.ui.active.selection

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.ui.LabelButton

@Composable
fun LabelSelectionList(
    labels: List<Label>,
    preSelected: List<Int>,
    onCheckedChange: (Int, Boolean) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(6.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        columns = GridCells.Adaptive(minSize = 150.dp)
    ) {
        items(
            items = labels,
            key = { it.id }
        ) { label ->
            var checked by remember {
                mutableStateOf(preSelected.contains(label.id))
            }
            LabelButton(
                item = label,
                onItemClick = {
                    checked = !checked
                    onCheckedChange(label.id, checked)
                },
                checked = preSelected.contains(label.id)
            )
        }
    }
}

private const val TAG = "LabelSelectionList"
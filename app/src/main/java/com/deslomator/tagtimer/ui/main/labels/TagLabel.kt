package com.deslomator.tagtimer.ui.main.labels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.action.LabelsTabAction
import com.deslomator.tagtimer.state.LabelsTabState
import com.deslomator.tagtimer.ui.LabelButton

@Composable
fun TagLabel(
    state: LabelsTabState,
    onAction: (LabelsTabAction) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(6.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        columns = GridCells.Adaptive(minSize = 150.dp)
    ) {
        items(
            items = state.tags,
            key = { it.id!! }
        ) { tag ->
            LabelButton(
                item = tag,
                onItemClick = { onAction(LabelsTabAction.EditTagClicked(tag)) },
            )
        }
    }
}
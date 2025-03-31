package com.deslomator.tagtimer.ui.active

import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.type.Checked

@Composable
fun SelectedLabelsList(
    labels: List<Label>,
    currentLabel: Label?,
    onItemClick: (Label) -> Unit
) {
    val rowState = rememberLazyListState()
    LazyRow(
        state = rowState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items(
            items = labels,
            key = { it.id!! }
        ) { label ->
            val checked by remember(currentLabel) {
                derivedStateOf { currentLabel == label }
            }
            LaunchedEffect(checked) {
                if (checked) rowState.animateScrollAndCentralizeItem(labels.indexOf(label))
            }
//            Log.d(TAG, "recomposing PersonButton, id: ${person.id}")
            LabelButton(
                label = label,
                onItemClick = { onItemClick(label) },
                checked = checked,
                checkType = Checked.SIZE,
                square = true,
            )
        }
    }
}

private const val TAG = "SelectedLabelsList"

private suspend fun LazyListState.animateScrollAndCentralizeItem(index: Int) {
    val itemInfo = this.layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }
    if (itemInfo != null) {
        val center = layoutInfo.viewportEndOffset / 2
        val childCenter = itemInfo.offset + itemInfo.size / 2
        animateScrollBy((childCenter - center).toFloat())
    } else {
        animateScrollToItem(index)
    }
}
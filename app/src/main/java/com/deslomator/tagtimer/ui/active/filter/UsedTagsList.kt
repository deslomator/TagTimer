package com.deslomator.tagtimer.ui.active.filter

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
fun UsedTagsList(
    modifier: Modifier,
    tags: List<Label.Tag>,
    currentTag: String,
    onItemClick: (String) -> Unit
) {
    Row(
        modifier = modifier.padding(start = 5.dp, end = 5.dp)
    ) {
        LazyVerticalGrid(
            contentPadding = PaddingValues(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            columns = GridCells.Adaptive(100.dp),
        ) {
            items(
                items = tags,
                key = { it.id }
            ) { tag ->
                val checked by remember(currentTag) {
                    derivedStateOf { currentTag == tag.name }
                }
//                Log.d(TAG, "recomposing TagButton, id: ${tag.id}")
                LabelButton(
                    item = tag,
                    onItemClick = { onItemClick(tag.name) },
                    checked = checked,
                    checkType = Checked.LEADING,
                )
            }
        }
    }
}

private const val TAG = "UsedTagsList"
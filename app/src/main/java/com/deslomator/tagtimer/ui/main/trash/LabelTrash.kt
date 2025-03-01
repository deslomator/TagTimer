package com.deslomator.tagtimer.ui.main.trash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.ui.LabelButton

@Composable
fun LabelTrash(
    items: List<Label>,
    onRestoreClick: (Label) -> Unit,
    onPurgeClick: (Label) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = items,
            key = { it.id!! }
        ) { label ->
            LabelButton(
                item = label,
                isTrash = true,
                onLeadingClick = { onRestoreClick(it) },
                onTrailingClick = { onPurgeClick(it) },
            )
        }
    }
}
package com.deslomator.tagtimer.ui.active.session

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.ui.LabelButton

@Composable
fun PreSelectedTagsList(
    modifier: Modifier,
    tags: List<Label.Tag>,
    onItemClicked: (Label.Tag) -> Unit,
) {
    if (tags.isEmpty()) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.tap_toolbar_icon_add_pst),
            textAlign = TextAlign.Center
        )
    }
    LazyVerticalGrid(
        modifier = modifier,
        contentPadding = PaddingValues(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        columns = GridCells.Adaptive(minSize = 150.dp)
    ) {
        items(
            items = tags,
            key = { it.id }
        ) { tag ->
            LabelButton(
                item = tag,
                onItemClick = { onItemClicked(tag) },
                checked = true
            )
        }
    }
}
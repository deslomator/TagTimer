package com.deslomator.tagtimer.ui.active

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.type.Checked
import com.deslomator.tagtimer.ui.EmptyListText

@Composable
fun TagsList(
    modifier: Modifier,
    tags: List<Label>,
    onItemClicked: (Label) -> Unit,
) {
    if (tags.isEmpty()) {
        EmptyListText(stringResource(id = R.string.tap_toolbar_icon_add_pst))
    }
    LazyVerticalGrid(
        modifier = modifier,
        contentPadding = PaddingValues(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        columns = GridCells.Fixed(5)
    ) {
        items(
            items = tags,
            key = { it.id!! }
        ) { tag ->
            LabelButton(
                label = tag,
                onItemClick = { onItemClicked(tag) },
                checked = false,
                checkType = Checked.NONE,
                square = true
            )
        }
    }
}
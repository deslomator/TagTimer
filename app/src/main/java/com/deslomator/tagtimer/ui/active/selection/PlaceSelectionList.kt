package com.deslomator.tagtimer.ui.active.selection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.ui.LabelButton

@Composable
fun PlaceSelectionList(
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(6.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        columns = GridCells.Adaptive(minSize = 150.dp)
    ) {
        items(
            items = state.places,
            key = { it.id }
        ) { place ->
            var checked by remember {
                mutableStateOf(state.preSelectedPlaces.map { it.placeId }.contains(place.id))
            }
            val onCheckedChange: () -> Unit = {
                checked = !checked
                onAction(ActiveSessionAction.SelectPlaceCheckedChange(place.id, checked))
            }
            LabelButton(
                modifier = Modifier.alpha(if (checked) 1F else .4F),
                item = place,
                onItemClick = { onCheckedChange() },
                checked = checked
            )
        }
    }
}
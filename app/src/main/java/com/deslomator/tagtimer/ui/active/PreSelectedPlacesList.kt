package com.deslomator.tagtimer.ui.active

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.model.Lbl
import com.deslomator.tagtimer.ui.LabelButton

@Composable
fun PreSelectedPlacesList(
    places: List<Lbl.Place>,
    currentPlace: String,
    onAction: (ActiveSessionAction) -> Unit
) {
    LazyRow(
        modifier = Modifier.padding(start = 5.dp, end = 5.dp),
    ) {
        items(
            items = places,
            key = { it.id }
        ) { place ->
            LabelButton(
                modifier = Modifier.width(100.dp),
                item = place,
                onItemClick = { onAction(ActiveSessionAction.PreSelectedPlaceClicked(place.name)) },
                checked = currentPlace == place.name
            )
        }
    }
}
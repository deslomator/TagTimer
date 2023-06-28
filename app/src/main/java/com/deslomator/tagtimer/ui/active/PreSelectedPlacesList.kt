package com.deslomator.tagtimer.ui.active

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.ui.MyListItem
import com.deslomator.tagtimer.ui.theme.contrasted

@Composable
fun PreSelectedPlacesList(
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit
) {
    LazyRow {
        items(
            items = state.places.filter { place ->
                state.preSelectedPlaces.map { it.placeId }.contains(place.id)
            },
            key = { it.id }
        ) { place ->
            MyListItem(
                leadingIcon = R.drawable.place,
                onLeadingClick = { onAction(ActiveSessionAction.PreSelectedPlaceClicked(place.name)) },
                colors = CardDefaults.cardColors(
                    contentColor = Color(place.color).contrasted(),
                    containerColor = Color(place.color),
                ),
                item = place,
                shape = CutCornerShape(18.dp),
                border = BorderStroke(1.dp, Color.LightGray),
                onItemClick = { onAction(ActiveSessionAction.PreSelectedPlaceClicked(place.name)) },
            ) { item ->
                Text(item.name)
            }
        }
    }
}
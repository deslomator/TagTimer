package com.deslomator.tagtimer.ui.active.selection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.ui.MyListItem
import com.deslomator.tagtimer.ui.theme.contrasted

@Composable
fun PlaceSelectionList(
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(6.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(
            items = state.places,
            key = { it.id }
        ) { place ->
            val checked = state.preSelectedPlaces.map { it.placeId }.contains(place.id)
            val onCheckedChange: (Boolean) -> Unit = { it ->
                onAction(ActiveSessionAction.SelectPlaceCheckedChange(place.id, it))
            }
            MyListItem(
                leadingIcon = R.drawable.place,
                colors = CardDefaults.cardColors(
                    contentColor = Color(place.color).contrasted(),
                    containerColor = Color(place.color),
                ),
                item = place,
                shape = CutCornerShape(18.dp),
                border = BorderStroke(1.dp, Color.LightGray),
            ) {
                Column(modifier = Modifier.weight(1.0f)) {
                    Text(place.name)
                }
                Checkbox(
                    checked = checked,
                    onCheckedChange = { onCheckedChange(it) }
                )
            }
        }
    }
}
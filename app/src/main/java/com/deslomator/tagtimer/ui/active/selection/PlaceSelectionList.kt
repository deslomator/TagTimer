package com.deslomator.tagtimer.ui.active.selection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.ui.MyButton
import com.deslomator.tagtimer.ui.theme.contrasted

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
            MyButton(
                modifier = Modifier.alpha(if (checked) 1F else .4F),
                leadingIcon = R.drawable.place,
                onLeadingClick = { onCheckedChange() },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color(place.color).contrasted(),
                    containerColor = Color(place.color),
                ),
                item = place,
                text = place.name,
                border = BorderStroke(1.dp, Color.LightGray),
                onItemClick = { onCheckedChange() },
            )
        }
    }
}
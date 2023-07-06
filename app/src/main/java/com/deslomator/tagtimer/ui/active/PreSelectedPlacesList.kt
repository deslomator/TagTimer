package com.deslomator.tagtimer.ui.active

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
fun PreSelectedPlacesList(
    places: List<Label.Place>,
    currentPlace: String,
    onItemClick: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier.padding(start = 5.dp, end = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(
            items = places,
            key = { it.id }
        ) { place ->
            val checked by remember(currentPlace) {
                derivedStateOf { currentPlace == place.name }
            }
//            Log.d(TAG, "recomposing PlaceButton, id: ${place.id}")
            LabelButton(
                modifier = Modifier.width(100.dp),
                item = place,
                onItemClick = { onItemClick(place.name) },
                checked = checked,
                checkType = Checked.LEADING,
            )
        }
    }
}

private const val TAG = "PreSelectedPlacesList"
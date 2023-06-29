package com.deslomator.tagtimer.ui.active

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.model.Place
import com.deslomator.tagtimer.ui.theme.contrasted

@Composable
fun PreSelectedPlacesList(
    places: List<Place>,
    currentPlace: String,
    onAction: (ActiveSessionAction) -> Unit
) {
    Row {
        places.forEach { place ->
            TextButton(
                modifier = Modifier
                    .weight(1F)
                    .alpha(if (currentPlace == place.name) 1F else 0.4F),
                onClick = { onAction(ActiveSessionAction.PreSelectedPlaceClicked(place.name)) },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color(place.color).contrasted(),
                    containerColor = Color(place.color),
                ),
                border = BorderStroke(1.dp, Color.LightGray),
            ) {
                Icon(
                    painterResource(id = R.drawable.place),
                    contentDescription = null
                )
                Text(text = place.name,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
            }
        }
    }
}
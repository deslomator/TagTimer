package com.deslomator.tagtimer.ui.main.labels.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.LabelsTabAction
import com.deslomator.tagtimer.model.Place
import com.deslomator.tagtimer.state.LabelsTabState
import com.deslomator.tagtimer.ui.ColorPicker
import com.deslomator.tagtimer.ui.MyDialog

@Composable
fun PlaceDialog(
    state: LabelsTabState,
    onAction: (LabelsTabAction) -> Unit,
    place: Place
) {
    var name by rememberSaveable { mutableStateOf(place.name) }
    var color by rememberSaveable { mutableStateOf(place.color) }
    MyDialog(
        onDismiss = { onAction(LabelsTabAction.DismissPlaceDialog) },
        onAccept = {
            val t = place.copy(
                name = name,
                color = color
            )
            onAction(LabelsTabAction.AcceptPlaceEditionClicked(t))
        }
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = if(state.isEditingPlace) R.string.edit_place
            else R.string.new_place),
            textAlign = TextAlign.Center
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = { name = it },
            placeholder = { Text(text = stringResource(id = R.string.name)) }
        )
        ColorPicker(
            selectedColor = Color(color),
            onItemClick = { color = it }
        )
    }
}

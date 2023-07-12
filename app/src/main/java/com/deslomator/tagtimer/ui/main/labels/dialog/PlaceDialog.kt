package com.deslomator.tagtimer.ui.main.labels.dialog

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.LabelsTabAction
import com.deslomator.tagtimer.state.LabelsTabState
import com.deslomator.tagtimer.ui.ColorPicker
import com.deslomator.tagtimer.ui.DialogTextField
import com.deslomator.tagtimer.ui.MyDialog
import com.deslomator.tagtimer.ui.showSnackbar
import kotlinx.coroutines.CoroutineScope

@Composable
fun PlaceDialog(
    state: LabelsTabState,
    onAction: (LabelsTabAction) -> Unit,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    val message = stringResource(id = R.string.place_sent_to_trash)
    var name by rememberSaveable { mutableStateOf(state.currentPlace.name) }
    var color by rememberSaveable { mutableIntStateOf(state.currentPlace.color) }
    MyDialog(
        onDismiss = { onAction(LabelsTabAction.DismissPlaceDialog) },
        onAccept = {
            val t = state.currentPlace.copy(
                name = name,
                color = color
            )
            onAction(LabelsTabAction.AcceptPlaceEditionClicked(t))
        },
        showTrash = state.isEditingPlace,
        onTrash = {
            showSnackbar(
                scope,
                snackbarHostState,
                message
            )
            onAction(LabelsTabAction.DeletePlaceClicked(state.currentPlace))
        },
        title = if(state.isEditingPlace) R.string.edit_place else R.string.new_place
    ) {
        DialogTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = R.string.name,
            icon = R.drawable.place
        )
        Spacer(modifier = Modifier.height(7.dp))
        ColorPicker(
            selectedColor = Color(color),
            onItemClick = { color = it }
        )
    }
}

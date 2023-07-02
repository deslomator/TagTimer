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
fun PersonDialog(
    state: LabelsTabState,
    onAction: (LabelsTabAction) -> Unit,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    val message = stringResource(id = R.string.person_sent_to_trash)
    var name by rememberSaveable { mutableStateOf(state.currentPerson.name) }
    var color by rememberSaveable { mutableIntStateOf(state.currentPerson.color) }
    MyDialog(
        onDismiss = { onAction(LabelsTabAction.DismissPersonDialog) },
        onAccept = {
            val t = state.currentPerson.copy(
                name = name,
                color = color
            )
            onAction(LabelsTabAction.AcceptPersonEditionClicked(t))
        },
        showTrash = state.isEditingPerson,
        onTrash = {
            showSnackbar(
                scope,
                snackbarHostState,
                message
            )
            onAction(LabelsTabAction.TrashPersonSwiped(state.currentPerson))
        },
        title = if(state.isEditingPerson) R.string.edit_person else R.string.new_person
    ) {
        DialogTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = R.string.name,
            icon = R.drawable.person
        )
        Spacer(modifier = Modifier.height(7.dp))
        ColorPicker(
            selectedColor = Color(color),
            onItemClick = { color = it }
        )
    }
}

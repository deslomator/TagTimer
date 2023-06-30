package com.deslomator.tagtimer.ui.main.labels.dialog

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.LabelsTabAction
import com.deslomator.tagtimer.model.Person
import com.deslomator.tagtimer.state.LabelsTabState
import com.deslomator.tagtimer.ui.ColorPicker
import com.deslomator.tagtimer.ui.DialogTextField
import com.deslomator.tagtimer.ui.MyDialog

@Composable
fun PersonDialog(
    state: LabelsTabState,
    onAction: (LabelsTabAction) -> Unit,
    person: Person
) {
    var name by rememberSaveable { mutableStateOf(person.name) }
    var color by rememberSaveable { mutableStateOf(person.color) }
    MyDialog(
        onDismiss = { onAction(LabelsTabAction.DismissPersonDialog) },
        onAccept = {
            val t = person.copy(
                name = name,
                color = color
            )
            onAction(LabelsTabAction.AcceptPersonEditionClicked(t))
        }
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = if(state.isEditingPerson) R.string.edit_person
            else R.string.new_person),
            textAlign = TextAlign.Center
        )
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

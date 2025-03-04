package com.deslomator.tagtimer.ui.active.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.model.type.DialogState
import com.deslomator.tagtimer.ui.MyDialog
import com.deslomator.tagtimer.ui.TimeNumberPicker

@Composable
fun TimeDialog(
    current: Long,
    onDismiss: () -> Unit,
    onAccept: (Long) -> Unit,
) {
    var value by rememberSaveable { mutableLongStateOf(current) }
    MyDialog(
        onDismiss = onDismiss,
        onAccept = { onAccept(value) },
        dialogState = DialogState.EDIT_NO_DELETE
    ) {
        Text(
            modifier = Modifier.fillMaxWidth() ,
            text = stringResource( R.string.set_time ),
            textAlign = TextAlign.Center
        )
        TimeNumberPicker(
            timeMillis = value,
            onValueChange = { value = it },
            enabled = true
        )
    }
}

private const val TAG = "TimeDialog"
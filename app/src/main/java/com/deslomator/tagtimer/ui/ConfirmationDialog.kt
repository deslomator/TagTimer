package com.deslomator.tagtimer.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.deslomator.tagtimer.R

@Composable
fun ConfirmationDialog(
    title: String,
    onAccept: () -> Unit,
    onCancel: () -> Unit,
) {

    AlertDialog(
        onDismissRequest = { onCancel() },
        title = {
            Text(
                text = title
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onAccept() }
            ) {
                Text(stringResource(id = R.string.accept))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {onCancel() }
            ) {
                Text(stringResource(id = R.string.cancel))
            }
        }
    )
}

package com.deslomator.tagtimer.ui.active

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.deslomator.tagtimer.toElapsedTime
import com.deslomator.tagtimer.ui.MyDialog

@Composable
fun TimeDialog(
    current: Float,
    maximum: Float,
    onDismiss: () -> Unit,
    onAccept: (Float) -> Unit,
) {
    var value by rememberSaveable { mutableStateOf(current) }
    val max by rememberSaveable { mutableStateOf(maximum) }
    MyDialog(
        onDismiss = onDismiss,
        onAccept = { onAccept(value) },
    ) {
        Text(
            modifier = Modifier.fillMaxWidth() ,
            text = value.toLong().toElapsedTime(),
            textAlign = TextAlign.Center
        )
        Slider(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            valueRange = 0F..max,
            onValueChange = { value = it }
        )
    }
}

private const val TAG = "TimeDialog"
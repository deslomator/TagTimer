package com.deslomator.tagtimer.ui

import android.widget.NumberPicker
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun AndroidNumberPicker(
    value: Int,
    min: Int = 0,
    max: Int = Int.MAX_VALUE,
    onValueChange: (Int) -> Unit,
    wrapWheel: Boolean = false
) {
    AndroidView(
        factory = { context ->
            NumberPicker(context).apply {
                setOnValueChangedListener { _, _, newVal ->
                    onValueChange(newVal)
                }
                minValue = min
                maxValue = max
                this.value = value
                wrapSelectorWheel = wrapWheel
                setFormatter { n -> String.format("%02d", n) }
            }
        },
        update = {}
    )
}
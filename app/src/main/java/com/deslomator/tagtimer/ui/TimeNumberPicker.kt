package com.deslomator.tagtimer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R

@Composable
fun TimeNumberPicker(
    timeMillis: Long,
    onValueChange: (Long) -> Unit,
    enabled: Boolean,
) {
    var hours by rememberSaveable { mutableIntStateOf(((timeMillis / 1000) / 3600).toInt()) }
    var minutes by rememberSaveable { mutableIntStateOf((((timeMillis / 1000) % 3600) / 60).toInt()) }
    var seconds by rememberSaveable { mutableIntStateOf((((timeMillis / 1000) % 3600) % 60).toInt()) }
    fun total() = ((hours * 3600 + minutes * 60 + seconds) * 1000).toLong()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(id = R.string.timestamp))
        Spacer(modifier = Modifier.width(15.dp))
        if (enabled) {
            AndroidNumberPicker(
                value = hours,
                max = 24,
                onValueChange = {
                    hours = it
                    onValueChange(total())
                }
            )
            Text(text = ":")
            AndroidNumberPicker(
                value = minutes,
                max = 59,
                onValueChange = {
                    minutes = it
                    onValueChange(total())
                },
                wrapWheel = true
            )
            Text(text = ":")
            AndroidNumberPicker(
                value = seconds,
                max = 59,
                onValueChange = {
                    seconds = it
                    onValueChange(total())
                },
                wrapWheel = true
            )
        } else {
            TextField(
                value = "${hours.toString().padStart(2, '0')} : " +
                    "${hours.toString().padStart(2, '0')} : " +
                    hours.toString().padStart(2, '0'),
                onValueChange = {},
                enabled = false
            )
        }
    }
}


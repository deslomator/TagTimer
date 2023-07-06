package com.deslomator.tagtimer.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.ui.theme.VeryLightGray

@Composable
fun MyDialog(
    onDismiss: () -> Unit,
    onAccept: () -> Unit,
    showTrash: Boolean = false,
    onTrash: (() -> Unit)? = null,
    showCopy: Boolean = false,
    onCopy: (() -> Unit)? = null,
    @StringRes title: Int? = null,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .background(Color.Black.copy(alpha = .65f))
            .clickable { onDismiss() }
            .fillMaxSize()
            .padding(top = 50.dp, bottom = 50.dp, start = 16.dp, end = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        SubcomposeLayout(
            modifier = Modifier
                .clickable { } // intercept clicks, only dismiss tapping the outer box
                .clip(shape = RoundedCornerShape(10.dp))
                .background(VeryLightGray)
                .padding(5.dp),
        ) { constraints ->
            val header = subcompose(3) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    title?.let {
                        Text(
                            modifier = Modifier.weight(1F),
                            text = stringResource(id = it),
                            textAlign = TextAlign.Center
                        )
                    }
                    if (showCopy) {
                        IconButton(onClick = { onCopy?.invoke() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.copy),
                                contentDescription = null
                            )
                        }
                    }
                    if (showTrash) {
                        IconButton(onClick = { onTrash?.invoke() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.delete),
                                contentDescription = null
                            )
                        }
                    }
                }
            }.map { it.measure(constraints) }.first()
            val footer = subcompose(1) {
                Row(
                    modifier = Modifier
                        .heightIn(min = 50.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = onAccept
                    ) {
                        Text(text = stringResource(id = R.string.accept))
                    }
                }
            }.map { it.measure(constraints) }.first()
            val list = subcompose(0) {
                Column {
                    content()
                }
            }.map {
                it.measure(
                    Constraints(
                        maxHeight = constraints.maxHeight - header.height - footer.height,
                        maxWidth = constraints.maxWidth
                    )
                )
            }.first()
            layout(
                width = constraints.maxWidth,
                height = list.height + header.height + footer.height
            ) {
                header.place(x = 0, y = 0)
                list.place(x = 0, y = header.height)
                footer.place(x = 0, y = header.height + list.height)
            }
        }
    }
}

private const val TAG = "MyDialog"
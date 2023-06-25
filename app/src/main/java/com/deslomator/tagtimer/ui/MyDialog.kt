package com.deslomator.tagtimer.ui

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.ui.theme.VeryLightGray

@Composable
fun MyDialog(
    onDismiss: () -> Unit,
    onAccept: () -> Unit,
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
//                .wrapContentSize()
                .clip(shape = RoundedCornerShape(10.dp))
                .background(VeryLightGray)
                .padding(5.dp),
        ) { constraints ->
            val footer = subcompose(1)
            {
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
            }.map {
                it.measure(constraints)
            }
            val footerHeight = footer[0].height
            val list = subcompose(0) {
                Column {
                    content()
                }
            }.map {
                it.measure(
                    Constraints(
                        maxHeight = constraints.maxHeight - footerHeight,
                        maxWidth = constraints.maxWidth
                    )
                )
            }
            val listHeight = list[0].height
            layout(
                width = constraints.maxWidth,
                height = listHeight + footerHeight
            ) {
                list[0].place(x = 0, y = 0)
                footer[0].place(x = 0, y = listHeight)
            }
        }
    }
}

private const val TAG = "MyDialog"
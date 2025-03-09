package com.deslomator.tagtimer.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.model.type.DialogState

@Composable
fun MyDialog(
    onDismiss: () -> Unit,
    onAccept: () -> Unit,
    dialogState: DialogState,
    canBeDeleted: Boolean = true,
    showCopy: Boolean = true,
    onCopyClicked: () -> Unit,
    onArchiveClicked: () -> Unit,
    onUnArchiveClicked: () -> Unit,
    onTrashClicked: () -> Unit,
    onUnTrashClicked: () -> Unit,
    onPurgeClicked: () -> Unit,
    @StringRes title: Int? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.scrim.copy(alpha = .65f))
            .clickable { onDismiss() }
            .fillMaxSize()
            .padding(top = 50.dp, bottom = 50.dp, start = 16.dp, end = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        SubcomposeLayout(
            modifier = Modifier
                .clickable { } // intercept clicks, only dismiss tapping the outer box
                .clip(shape = RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
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
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    if (
                        (dialogState == DialogState.ENABLED ||
                                dialogState == DialogState.ARCHIVED) &&
                        showCopy
                    ) {
                        IconButton(onClick = onCopyClicked) {
                            Icon(
                                painter = painterResource(id = R.drawable.copy),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                    if (dialogState == DialogState.ENABLED) {
                        IconButton(onClick = onArchiveClicked) {
                            Icon(
                                painter = painterResource(id = R.drawable.archive),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                    if (dialogState == DialogState.ARCHIVED) {
                        IconButton(onClick = onUnArchiveClicked) {
                            Icon(
                                painter = painterResource(id = R.drawable.unarchive),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                    Box(
                        modifier = Modifier.wrapContentSize()
                    ) {
                        if (dialogState == DialogState.ENABLED || dialogState == DialogState.ARCHIVED) {
                            IconButton(onClick = onTrashClicked) {
                                Icon(
                                    painter = painterResource(id = R.drawable.trash),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                        if (!canBeDeleted) {
                            IconButton(
                                onClick = onTrashClicked
                            ) {
                                Icon(
                                    modifier = Modifier.size(35.dp),
                                    painter = painterResource(id = R.drawable.forbidden),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error,
                                )
                            }
                        }
                    }
                    if (dialogState == DialogState.TRASHED) {
                        IconButton(onClick = onUnTrashClicked) {
                            Icon(
                                painter = painterResource(id = R.drawable.untrash),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                    if (dialogState == DialogState.TRASHED) {
                        IconButton(onClick = onPurgeClicked) {
                            Icon(
                                painter = painterResource(id = R.drawable.purge),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
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
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onAccept
                    ) {
                        Text(text = stringResource(id = R.string.accept))
                    }
                }
            }.map { it.measure(constraints) }.first()
            val list = subcompose(0) {
                Column {
                    this.content()
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
package com.deslomator.tagtimer.ui.active.selection

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.model.type.DialogState
import com.deslomator.tagtimer.model.type.LabelSort
import com.deslomator.tagtimer.model.type.LabelType
import com.deslomator.tagtimer.ui.theme.largeTopBarColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelSelectionTopBar(
    labelType: LabelType,
    onAddLabelClick: (LabelType) -> Unit,
    dialogState: DialogState,
    tagSort: LabelSort,
    personSort: LabelSort,
    placeSort: LabelSort,
    onTagSort: (LabelSort) -> Unit,
    onPersonSort: (LabelSort) -> Unit,
    onPlaceSort: (LabelSort) -> Unit,
    showEnabled: Boolean,
    onShowEnabledClick: (Boolean) -> Unit,
    showArchived: Boolean,
    onShowArchivedClick: (Boolean) -> Unit,
    showTrashed: Boolean,
    onShowTrashedClick: (Boolean) -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        TopAppBar(
            actions = {
                if (dialogState == DialogState.HIDDEN) {
                    Row {
                        AnimatedContent(labelType) { current ->
                            IconButton(
                                onClick = { onAddLabelClick(current) }
                            ) {
                                Icon(
                                    modifier = Modifier.size(40.dp),
                                    painter = painterResource(current.addIconId),
                                    contentDescription = stringResource(id = current.addStringId)
                                )
                            }
                        }
                        IconButton(
                            onClick = { showMenu = !showMenu }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.more_vert),
                                contentDescription = null
                            )
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            LabelSort.entries.forEach {
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            RadioButton(
                                                selected =
                                                    it == when (labelType) {
                                                        LabelType.TAG -> tagSort
                                                        LabelType.PERSON -> personSort
                                                        LabelType.PLACE -> placeSort
                                                    },
                                                onClick = {
                                                    when (labelType) {
                                                        LabelType.TAG -> onTagSort(it)
                                                        LabelType.PERSON -> onPersonSort(it)
                                                        LabelType.PLACE -> onPlaceSort(it)
                                                    }
                                                    showMenu = false
                                                },
                                            )
                                            Text(text = stringResource(it.stringId))
                                        }
                                    },
                                    onClick = {
                                        when (labelType) {
                                            LabelType.TAG -> onTagSort(it)
                                            LabelType.PERSON -> onPersonSort(it)
                                            LabelType.PLACE -> onPlaceSort(it)
                                        }
                                        showMenu = false
                                    },
                                )
                            }
                            HorizontalDivider()
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = showEnabled,
                                            onCheckedChange = {
                                                onShowEnabledClick(it)
                                            }
                                        )
                                        Text(text = stringResource(R.string.show_active))
                                    }
                                },
                                onClick = {
                                    onShowEnabledClick(!showEnabled)
                                },
                            )
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = showArchived,
                                            onCheckedChange = {
                                                onShowArchivedClick(it)
                                            }
                                        )
                                        Text(text = stringResource(R.string.show_archived))
                                    }
                                },
                                onClick = {
                                    onShowArchivedClick(!showArchived)
                                },
                            )
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = showTrashed,
                                            onCheckedChange = {
                                                onShowTrashedClick(it)
                                            }
                                        )
                                        Text(text = stringResource(R.string.show_trashed))
                                    }
                                },
                                onClick = {
                                    onShowTrashedClick(!showTrashed)
                                },
                            )
                        }
                    }
                }
            },
            colors = largeTopBarColors(),
            title = {},
        )
    }
}

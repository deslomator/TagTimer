package com.deslomator.tagtimer.ui.active.selection

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
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
import kotlin.enums.EnumEntries

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelSelectionTopBar(
    pages: EnumEntries<LabelType>,
    currentPage: Int,
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
            modifier = Modifier.height(80.dp),
            actions = {
                if (dialogState == DialogState.HIDDEN) {
                    AnimatedContent(pages[currentPage]) { current ->
                        Row {
                            IconButton(
                                onClick = { onAddLabelClick(current) }
                            ) {
                                Icon(
                                    modifier = Modifier.size(40.dp),
                                    painter = painterResource(current.addIconId),
                                    contentDescription = stringResource(id = current.addStringId)
                                )
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
                                                        it == when (current) {
                                                            LabelType.TAG -> tagSort
                                                            LabelType.PERSON -> personSort
                                                            LabelType.PLACE -> placeSort
                                                        },
                                                    onClick = {
                                                        when (current) {
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
                                            when (current) {
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
                }
            },
            colors = largeTopBarColors(),
            title = {},
        )
    }
}

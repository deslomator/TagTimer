package com.deslomator.tagtimer.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
fun LabelsTopBar(
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
    showArchived: Boolean,
    onShowArchivedChanged: (Boolean) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        TopAppBar(
            modifier = Modifier.height(80.dp),
            actions = {
                if (dialogState == DialogState.HIDDEN) {
                    AnimatedContent(pages[currentPage]) { current ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    modifier = Modifier.size(40.dp),
                                    painter = painterResource(R.drawable.sort),
                                    contentDescription = null
                                )
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    LabelSort.entries.forEach {
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
                                                }
                                            )
                                            Text(text = stringResource(it.stringId))
                                        }
                                    }
                                }
                            }
                            Column(
                                modifier = Modifier.fillMaxHeight(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceAround
                            ) {
                                Text(text = stringResource(R.string.show_archived))
                                Checkbox(
                                    checked = showArchived,
                                    onCheckedChange = {
                                        onShowArchivedChanged(it)
                                    }
                                )
                            }
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
                    }
                }
            },
            colors = largeTopBarColors(),
            title = {},
        )
    }
}

@Composable
private fun SortMenu(
    currentSort: LabelSort,
    onItemClick: (LabelSort) -> Unit,
    showArchived: Boolean,
    onShowArchivedChanged: (Boolean) -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }
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
                            selected = currentSort == it,
                            onClick = {
                                showMenu = false
                                onItemClick(it)
                            }
                        )
                        Text(text = stringResource(it.stringId))
                    }
                },
                onClick = {
                    showMenu = false
                    onItemClick(it)
                },
            )
        }
        DropdownMenuItem(
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = showArchived,
                        onCheckedChange = {
                            showMenu = false
                            onShowArchivedChanged(it)
                        }
                    )
                    Text(text = stringResource(R.string.show_archived))
                }
            },
            onClick = {
                showMenu = false
                onShowArchivedChanged(!showArchived)
            },
        )
    }
}

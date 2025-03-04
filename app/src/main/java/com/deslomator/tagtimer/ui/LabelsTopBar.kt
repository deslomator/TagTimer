package com.deslomator.tagtimer.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.model.type.DialogState
import com.deslomator.tagtimer.model.type.LabelSort
import com.deslomator.tagtimer.model.type.LabelType
import com.deslomator.tagtimer.ui.theme.topBarColors
import kotlin.enums.EnumEntries

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelsTopBar(
    title: String,
    onBackClicked: (() -> Unit)?,
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
) {
    TopAppBar(
        navigationIcon = {
            onBackClicked?.let {
                IconButton(
                    onClick = it
                ) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_back),
                        contentDescription = "navigate back"
                    )
                }
            }
        },
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        actions = {
            if (dialogState == DialogState.HIDDEN) {
                AnimatedContent(pages[currentPage]) { current ->
                    Row {
                        IconButton(
                            onClick = { onAddLabelClick(current) }
                        ) {
                            Icon(
                                painter = painterResource(current.addIconId),
                                contentDescription = stringResource(id = current.addStringId)
                            )
                        }
                        SortMenu(
                            currentSort = when (current) {
                                LabelType.TAG -> tagSort
                                LabelType.PERSON -> personSort
                                LabelType.PLACE -> placeSort
                            },
                            onItemClick = {
                                when (current) {
                                    LabelType.TAG -> onTagSort(it)
                                    LabelType.PERSON -> onPersonSort(it)
                                    LabelType.PLACE -> onPlaceSort(it)
                                }
                            }
                        )
                    }
                }
            }
        },
        colors = topBarColors()
    )
}

@Composable
private fun SortMenu(
    currentSort: LabelSort,
    onItemClick: (LabelSort) -> Unit,
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
    }
}

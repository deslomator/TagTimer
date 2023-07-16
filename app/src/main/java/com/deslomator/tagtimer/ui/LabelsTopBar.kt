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
import com.deslomator.tagtimer.model.type.Sort
import com.deslomator.tagtimer.ui.theme.topBarColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelsTopBar(
    title: String,
    onBackClicked: (() -> Unit)?,
    currentPage: Int,
    onAddTagClick: () -> Unit,
    onAddPersonClick: () -> Unit,
    onAddPlaceClick: () -> Unit,
    showTagDialog: Boolean,
    showPersonDialog: Boolean,
    showPlaceDialog: Boolean,
    tagSort: Sort,
    personSort: Sort,
    placeSort: Sort,
    onTagSort: (Sort) -> Unit,
    onPersonSort: (Sort) -> Unit,
    onPlaceSort: (Sort) -> Unit,
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
        title = { Text(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        ) },
        actions = {
            if (!showTagDialog && !showPersonDialog && !showPlaceDialog) {
                AnimatedContent(currentPage) {
                    when (it) {
                        0 -> {
                            Row {
                                IconButton(
                                    onClick = onAddTagClick
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.add_tag),
                                        contentDescription = stringResource(id = R.string.add_tag)
                                    )
                                }
                                SortMenu(
                                    currentSort = tagSort,
                                    onNameSortClick = {
                                        onTagSort(Sort.NAME)
                                    },
                                    onColorSortClick = {
                                        onTagSort(Sort.COLOR)
                                    }
                                )
                            }
                        }
                        1 -> {
                            Row {
                                IconButton(
                                    onClick = onAddPersonClick
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.add_person),
                                        contentDescription = stringResource(id = R.string.add_person)
                                    )
                                }
                                SortMenu(
                                    currentSort = personSort,
                                    onNameSortClick = {
                                        onPersonSort(Sort.NAME)
                                    },
                                    onColorSortClick = {
                                        onPersonSort(Sort.COLOR)
                                    }
                                )
                            }
                        }
                        else -> {
                            Row {
                                IconButton(
                                    onClick = onAddPlaceClick
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.add_place),
                                        contentDescription = stringResource(id = R.string.add_place)
                                    )
                                }
                                SortMenu(
                                    currentSort = placeSort,
                                    onNameSortClick = {
                                        onPlaceSort(Sort.NAME)
                                    },
                                    onColorSortClick = {
                                        onPlaceSort(Sort.COLOR)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        colors = topBarColors()
    )
}

@Composable
private fun SortMenu(
    currentSort: Sort,
    onNameSortClick: () -> Unit,
    onColorSortClick: () -> Unit,
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
        DropdownMenuItem(
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = currentSort == Sort.NAME,
                        onClick = {
                            showMenu = false
                            onNameSortClick()
                        }
                    )
                    Text(text = stringResource(R.string.sort_by_name))
                }
            },
            onClick = {
                showMenu = false
                onNameSortClick()
            },
        )
        DropdownMenuItem(
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = currentSort == Sort.COLOR,
                        onClick = {
                            showMenu = false
                            onColorSortClick()
                        }
                    )
                    Text(text = stringResource(R.string.sort_by_color))
                }
            },
            onClick = {
                showMenu = false
                onColorSortClick()
            },
        )
    }
}

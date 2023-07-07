package com.deslomator.tagtimer.ui.active.selection

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
import com.deslomator.tagtimer.action.SharedAction
import com.deslomator.tagtimer.model.type.Sort
import com.deslomator.tagtimer.state.SharedState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelSelectionTopBar(
    title: String,
    onBackClicked: () -> Unit,
    currentPage: Int,
    onAddTagClick: () -> Unit,
    onAddPersonClick: () -> Unit,
    onAddPlaceClick: () -> Unit,
    showTagDialog: Boolean,
    showPersonDialog: Boolean,
    showPlaceDialog: Boolean,
    sharedState: SharedState,
    onSharedAction: (SharedAction) -> Unit,
) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onBackClicked
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_back),
                    contentDescription = "navigate back"
                )
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
                                    currentSort = sharedState.tagSort,
                                    onNameSortClick = {
                                        onSharedAction(SharedAction.TagSortClicked(Sort.NAME))
                                    },
                                    onColorSortClick = {
                                        onSharedAction(SharedAction.TagSortClicked(Sort.COLOR))
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
                                    currentSort = sharedState.personSort,
                                    onNameSortClick = {
                                        onSharedAction(SharedAction.PersonSortClicked(Sort.NAME))
                                    },
                                    onColorSortClick = {
                                        onSharedAction(SharedAction.PersonSortClicked(Sort.COLOR))
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
                                    currentSort = sharedState.placeSort,
                                    onNameSortClick = {
                                        onSharedAction(SharedAction.PlaceSortClicked(Sort.NAME))
                                    },
                                    onColorSortClick = {
                                        onSharedAction(SharedAction.PlaceSortClicked(Sort.COLOR))
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
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

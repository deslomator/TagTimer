package com.deslomator.tagtimer.ui.active

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavBackStackEntry
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.navigation.screen.ActiveNavigationScreen
import com.deslomator.tagtimer.state.ActiveSessionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveSessionTopBar(
    backStackEntry: State<NavBackStackEntry?>,
    state: ActiveSessionState,
    onBackClicked: () -> Unit,
    onEditSessionClick: () -> Unit,
    onShareSessionClick: () -> Unit,
    onAddTagClick: () -> Unit,
    onFilterClick: () -> Unit,
    onEventTrashClick: () -> Unit,
    onShareFilteredEventsClick: () -> Unit,
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
            text = state.currentSession.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        ) },
        actions = {
            when (backStackEntry.value?.destination?.route) {
                ActiveNavigationScreen.ActiveSession.route -> {
                    if (
                        !state.showTagsDialog &&
                        !state.showEventTrash &&
                        !state.showSessionEditionDialog &&
                        !state.showEventEditionDialog &&
                        !state.showEventInTrashDialog &&
                        !state.showTimeDialog
                    ) {
                        IconButton(
                            onClick = onShareSessionClick
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.share),
                                contentDescription = "share Session"
                            )
                        }
                        IconButton(
                            onClick = onEditSessionClick
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.edit),
                                contentDescription = "edit Session"
                            )
                        }
                        IconButton(
                            onClick = onAddTagClick
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.add_tag),
                                contentDescription = stringResource(id = R.string.add_tag)
                            )
                        }
                        IconButton(
                            onClick = onFilterClick
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.filter),
                                contentDescription = stringResource(id = R.string.filter_events)
                            )
                        }
                        IconButton(
                            onClick = onEventTrashClick
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.delete),
                                contentDescription = stringResource(id = R.string.trash)
                            )
                        }
                    }
                }
                ActiveNavigationScreen.EventFilter.route -> {
                    IconButton(
                        onClick = { onShareFilteredEventsClick() }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.share),
                            contentDescription = "share Events"
                        )
                    }
                }
            }
        }
    )
}

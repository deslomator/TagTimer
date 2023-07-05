package com.deslomator.tagtimer.ui.active.session

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.state.ActiveSessionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveSessionTopBar(
    state: ActiveSessionState,
    onBackClicked: () -> Unit,
    onShareSessionClick: () -> Unit,
    onAddLabelClick: () -> Unit,
    onFilterClick: () -> Unit,
    onEventTrashClick: () -> Unit,
) {
    var showMenu by remember {
        mutableStateOf(false)
    }
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
            if (
                !state.showEventEditionDialog &&
                !state.showTimeDialog
            ) {
                IconButton(
                    onClick = onAddLabelClick
                ) {
                    Icon(
                        painter = painterResource(R.drawable.add_tag),
                        contentDescription = stringResource(id = R.string.add_tag)
                    )
                }
                IconButton(
                    onClick = { showMenu = !showMenu }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.more_vert),
                        contentDescription = stringResource(id = R.string.add_tag)
                    )
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.share_session)) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.share),
                                contentDescription = "share Session"
                            )
                        },
                        onClick = onShareSessionClick,
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.filter_events)) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.filter),
                                contentDescription = "filter events"
                            )
                        },
                        onClick = onFilterClick,
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.event_trash)) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.delete),
                                contentDescription = "event trash"
                            )
                        },
                        onClick = onEventTrashClick,
                    )
                }
            }
        }
    )
}

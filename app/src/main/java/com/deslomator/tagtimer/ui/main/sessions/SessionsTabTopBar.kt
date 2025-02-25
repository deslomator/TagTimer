package com.deslomator.tagtimer.ui.main.sessions

import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.model.type.SessionSort
import com.deslomator.tagtimer.ui.theme.topBarColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionsTabTopBar(
    onNewSessionClick: () -> Unit,
    onPopulateDbClick: () -> Unit,
    onBackupClick: () -> Unit,
    onSessionSortClick: (SessionSort) -> Unit,
    currentSort: SessionSort
) {
    var showMenu by rememberSaveable { mutableStateOf(false) }
    TopAppBar(
        title = { Text(stringResource(id = R.string.app_name)) },
        actions = {
            IconButton(
                onClick = onNewSessionClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.document_and_ray_add),
                    contentDescription = stringResource(id = R.string.new_session)
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
                    text = { Text(text = stringResource(R.string.backup_restore)) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.backup),
                            contentDescription = "backup DB"
                        )
                    },
                    onClick = {
                        onBackupClick()
                        showMenu = false
                    },
                )
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.populate_db)) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.add_sessions),
                            contentDescription = "populate DB"
                        )
                    },
                    onClick = {
                        onPopulateDbClick()
                        showMenu = false
                    },
                )
                HorizontalDivider()
                SessionSort.entries.forEach {
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = currentSort == it,
                                    onClick = {
                                        showMenu = false
                                        onSessionSortClick(it)
                                    }
                                )
                                Text(text = stringResource(it.stringId))
                            }
                        },
                        onClick = {
                            showMenu = false
                            onSessionSortClick(it)
                        },
                    )
                }
            }
        },
        colors = topBarColors()
    )
}

package com.deslomator.tagtimer.ui.main.sessions

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
import com.deslomator.tagtimer.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionsTabTopBar(
    onNewSessionClick: () -> Unit,
    onPopulateDbClick: () -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }
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
                    text = { Text(text = stringResource(R.string.populate_db)) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.add_sessions),
                            contentDescription = "populate DB"
                        )
                    },
                    onClick = onPopulateDbClick,
                )
            }
        }
    )
}

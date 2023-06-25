package com.deslomator.tagtimer.ui.main

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.model.Trash
import com.deslomator.tagtimer.state.TrashTabState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    backStackEntry: State<NavBackStackEntry?>,
    onNewSessionClick: () -> Unit,
    onNewTagClick: () -> Unit,
    trashState: TrashTabState,
    onShowSessionsClick: () -> Unit,
    onShowTagsClick: () -> Unit,
) {
    TopAppBar(
        title = { Text(stringResource(id = R.string.app_name)) },
        actions = {
            when (backStackEntry.value?.destination?.route) {
                BottomNavigationScreen.Sessions.route -> {
                    IconButton(
                        onClick = onNewSessionClick
                    ) {
                        Icon(
                            modifier = Modifier.size(36.dp),
                            painter = painterResource(R.drawable.document_and_ray_add),
                            contentDescription = stringResource(id = R.string.new_session)
                        )
                    }
                }
                BottomNavigationScreen.Tags.route -> {
                    IconButton(
                        onClick = onNewTagClick
                    ) {
                        Icon(
                            modifier = Modifier.size(36.dp),
                            painter = painterResource(R.drawable.add_tag),
                            contentDescription = stringResource(id = R.string.new_tag)
                        )
                    }
                }
                BottomNavigationScreen.Trash.route -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = trashState.currentTrash == Trash.SESSION,
                            onClick = onShowSessionsClick
                        )
                        Text(stringResource(id = R.string.sessions))
                        Spacer(modifier = Modifier.size(20.dp))
                        RadioButton(
                            selected = trashState.currentTrash == Trash.TAG,
                            onClick = onShowTagsClick
                        )
                        Text(stringResource(id = R.string.tags))
                        Spacer(modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    )
}
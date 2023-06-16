package com.deslomator.tagtimer.ui.sessions

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.deslomator.tagtimer.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionsTopBar(
    onNewSessionClick: () -> Unit,
    onManageTagsClick: () -> Unit,
) {
    TopAppBar(
        title = { Text(stringResource(id = R.string.app_name)) },
        actions = {
            IconButton(
                onClick = onNewSessionClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_playlist_add_24),
                    contentDescription = stringResource(id = R.string.new_session)
                )
            }
            IconButton(
                onClick = onManageTagsClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_label_24),
                    contentDescription = stringResource(id = R.string.manage_tags)
                )
            }
        }
    )
}
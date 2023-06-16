package com.deslomator.tagtimer.ui.sessionsTrash

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.deslomator.tagtimer.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionsTrashTopBar(
    onBackClick: () -> Unit,
) {
    TopAppBar(
        title = { Text(stringResource(id = R.string.sessions_trash)) },
        navigationIcon = {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.go_back)
                )
            }
        }
    )
}
package com.deslomator.tagtimer.ui.backup

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.ui.theme.topBarColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupTopBar(
    onBackClicked: () -> Unit,
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
            text = stringResource(id = R.string.backup_restore),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        ) },
        colors = topBarColors()
    )
}

private const val TAG = "BackupTopBar"
package com.deslomator.tagtimer.ui.active

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveSessionTopBar(
    onBackClicked: () -> Unit,
    onNewSessionClick: () -> Unit,
    onNewTagClick: () -> Unit,
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
        title = { Text(stringResource(id = R.string.active_session)) },
        actions = {
            IconButton(
                onClick = onNewSessionClick
            ) {
                Icon(
                    modifier = Modifier.size(36.dp),
                    painter = painterResource(R.drawable.document_and_ray_add),
                    contentDescription = stringResource(id = R.string.new_session)
                )
            }
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
    )
}

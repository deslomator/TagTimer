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
import com.deslomator.tagtimer.state.ActiveSessionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveSessionTopBar(
    state: ActiveSessionState,
    onBackClicked: () -> Unit,
    onEditSessionClick: () -> Unit,
    onAddTagClick: () -> Unit,
    onEventTrashClick: () -> Unit,
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
        title = { Text(state.currentSession.name) },
        actions = {
            if (!state.showTagsDialog && !state.showEventTrash) {
                IconButton(
                    onClick = onEditSessionClick
                ) {
                    Icon(
                        modifier = Modifier.size(36.dp),
                        painter = painterResource(R.drawable.edit),
                        contentDescription = "edit Session"
                    )
                }
            }
            if (!state.showTagsDialog && !state.showEventTrash) {
                IconButton(
                    onClick = onAddTagClick
                ) {
                    Icon(
                        modifier = Modifier.size(36.dp),
                        painter = painterResource(R.drawable.add_tag),
                        contentDescription = stringResource(id = R.string.add_tag)
                    )
                }
            }
            if (!state.showTagsDialog && !state.showEventTrash) {
                IconButton(
                    onClick = onEventTrashClick
                ) {
                    Icon(
                        modifier = Modifier.size(36.dp),
                        painter = painterResource(R.drawable.delete),
                        contentDescription = stringResource(id = R.string.trash)
                    )
                }
            }
        }
    )
}

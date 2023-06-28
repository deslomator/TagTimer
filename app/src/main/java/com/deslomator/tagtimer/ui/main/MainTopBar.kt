package com.deslomator.tagtimer.ui.main

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.model.Trash
import com.deslomator.tagtimer.state.TrashTabState
import com.deslomator.tagtimer.ui.theme.PurpleGrey40
import com.deslomator.tagtimer.ui.theme.contrasted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    backStackEntry: State<NavBackStackEntry?>,
    onNewSessionClick: () -> Unit,
    onNewTagClick: () -> Unit,
    trashState: TrashTabState,
    onTrashTypeClick: (Trash) -> Unit,
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
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Spacer(modifier = Modifier.width(15.dp))
                        TrashButton(
                            modifier = Modifier.weight(1F),
                            type = Trash.SESSION,
                            currentTrash = trashState.currentTrash,
                            onTrashTypeClick = { onTrashTypeClick(it) },
                            label = R.string.sessions
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        TrashButton(
                            modifier = Modifier.weight(1F),
                            type = Trash.TAG,
                            currentTrash = trashState.currentTrash,
                            onTrashTypeClick = { onTrashTypeClick(it) },
                            label = R.string.tags
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        TrashButton(
                            modifier = Modifier.weight(1F),
                            type = Trash.PERSON,
                            currentTrash = trashState.currentTrash,
                            onTrashTypeClick = { onTrashTypeClick(it) },
                            label = R.string.persons
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        TrashButton(
                            modifier = Modifier.weight(1F),
                            type = Trash.PLACE,
                            currentTrash = trashState.currentTrash,
                            onTrashTypeClick = { onTrashTypeClick(it) },
                            label = R.string.places
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                    }
                }
            }
        }
    )
}

@Composable
private fun TrashButton(
    modifier: Modifier,
    type: Trash,
    currentTrash: Trash,
    onTrashTypeClick: (Trash) -> Unit,
    @StringRes label: Int
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(5.dp))
            .background(PurpleGrey40)
            .alpha(if (currentTrash == type) 1F else 0.5F)
            .clickable { onTrashTypeClick(type) }
            .padding(top= 5.dp, bottom = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = label),
            color = PurpleGrey40.contrasted()
        )
    }
}
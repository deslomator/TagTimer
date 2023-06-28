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
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Trash
import com.deslomator.tagtimer.state.LabelsTabState
import com.deslomator.tagtimer.state.TrashTabState
import com.deslomator.tagtimer.ui.theme.PurpleGrey40
import com.deslomator.tagtimer.ui.theme.contrasted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    backStackEntry: State<NavBackStackEntry?>,
    onNewSessionClick: () -> Unit,
    onNewTagClick: () -> Unit,
    onNewPersonClick: () -> Unit,
    onNewPlaceClick: () -> Unit,
    trashState: TrashTabState,
    onTrashTypeClick: (Trash) -> Unit,
    labelsTabState: LabelsTabState,
    onLabelTypeClick: (Label) -> Unit
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
                BottomNavigationScreen.Labels.route -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Spacer(modifier = Modifier.width(15.dp))
                        TopButton(
                            modifier = Modifier.weight(1F),
                            type = Label.TAG,
                            currentType = labelsTabState.currentLabel,
                            onTypeClick = { onLabelTypeClick(it) },
                            text = R.string.tags
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        TopButton(
                            modifier = Modifier.weight(1F),
                            type = Label.PERSON,
                            currentType = labelsTabState.currentLabel,
                            onTypeClick = { onLabelTypeClick(it) },
                            text = R.string.persons
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        TopButton(
                            modifier = Modifier.weight(1F),
                            type = Label.PLACE,
                            currentType = labelsTabState.currentLabel,
                            onTypeClick = { onLabelTypeClick(it) },
                            text = R.string.places
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        if (labelsTabState.currentLabel == Label.TAG) {
                            IconButton(
                                onClick = onNewTagClick
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.add_tag),
                                    contentDescription = stringResource(id = R.string.new_tag)
                                )
                            }
                        }
                        if (labelsTabState.currentLabel == Label.PERSON) {
                            IconButton(
                                onClick = onNewPersonClick
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.add_person),
                                    contentDescription = stringResource(id = R.string.new_person)
                                )
                            }
                        }
                        if (labelsTabState.currentLabel == Label.PLACE) {
                            IconButton(
                                onClick = onNewPlaceClick
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.add_place),
                                    contentDescription = stringResource(id = R.string.new_place)
                                )
                            }
                        }
                    }
                }
                BottomNavigationScreen.Trash.route -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Spacer(modifier = Modifier.width(15.dp))
                        TopButton(
                            modifier = Modifier.weight(1F),
                            type = Trash.SESSION,
                            currentType = trashState.currentTrash,
                            onTypeClick = { onTrashTypeClick(it) },
                            text = R.string.sessions
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        TopButton(
                            modifier = Modifier.weight(1F),
                            type = Trash.TAG,
                            currentType = trashState.currentTrash,
                            onTypeClick = { onTrashTypeClick(it) },
                            text = R.string.tags
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        TopButton(
                            modifier = Modifier.weight(1F),
                            type = Trash.PERSON,
                            currentType = trashState.currentTrash,
                            onTypeClick = { onTrashTypeClick(it) },
                            text = R.string.persons
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        TopButton(
                            modifier = Modifier.weight(1F),
                            type = Trash.PLACE,
                            currentType = trashState.currentTrash,
                            onTypeClick = { onTrashTypeClick(it) },
                            text = R.string.places
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                    }
                }
            }
        }
    )
}

@Composable
fun <T>TopButton(
    modifier: Modifier,
    type: T,
    currentType: T,
    onTypeClick: (T) -> Unit,
    @StringRes text: Int
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(5.dp))
            .background(PurpleGrey40)
            .alpha(if (currentType == type) 1F else 0.5F)
            .clickable { onTypeClick(type) }
            .padding(top= 5.dp, bottom = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = text),
            color = PurpleGrey40.contrasted()
        )
    }
}
package com.deslomator.tagtimer.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.deslomator.tagtimer.navigation.screen.BottomNavigationScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    backStackEntry: State<NavBackStackEntry?>,
    onNewSessionClick: () -> Unit,
    onNewTagClick: () -> Unit,
    onNewPersonClick: () -> Unit,
    onNewPlaceClick: () -> Unit,
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
                        IconButton(
                            onClick = onNewTagClick
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.add_tag),
                                contentDescription = stringResource(id = R.string.new_tag)
                            )
                        }
                        IconButton(
                            onClick = onNewPersonClick
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.add_person),
                                contentDescription = stringResource(id = R.string.new_person)
                            )
                        }
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
        }
    )
}

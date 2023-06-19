package com.deslomator.tagtimer.ui.sessions

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.SessionsScreenAction
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.state.SessionsScreenState
import com.deslomator.tagtimer.toDateTime
import com.deslomator.tagtimer.ui.MyListItem
import com.deslomator.tagtimer.ui.SwipeableListItem
import com.deslomator.tagtimer.ui.theme.Pink80
import com.deslomator.tagtimer.ui.theme.brightness
import com.deslomator.tagtimer.ui.theme.colorPickerColors
import com.deslomator.tagtimer.ui.theme.contrasted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionsScreenContent(
    paddingValues: PaddingValues,
    outerNavHostController: NavHostController,
    state: SessionsScreenState,
    onAction: (SessionsScreenAction) -> Unit
) {
    if (state.showSessionDialog) {
        SessionDialog(
            state = state,
            onAction = onAction,
            session = state.currentSession
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = state.sessions,
                key = { it.id }
            ) { session ->
                SwipeableListItem(
                    dismissDirection = DismissDirection.StartToEnd,
                    onDismiss = { onAction(SessionsScreenAction.TrashSessionSwiped(session)) },
                    dismissColor = Pink80
                ) { dismissState ->
                    MyListItem(
                        modifier = Modifier
                            .border(1.dp, Color.LightGray),
                        colors = ListItemDefaults.colors(
                            leadingIconColor = Color(session.color).contrasted(),
                            headlineColor = Color(session.color).contrasted(),
                            trailingIconColor = Color(session.color).contrasted(),
                            containerColor = Color(session.color),
                        ),
                        item = session,
                        leadingIcon = R.drawable.document_and_ray,
                        onItemClick = { outerNavHostController.navigate("hello") },
                        trailingIcon = R.drawable.baseline_edit_24,
                        onTrailingClick = {
                            onAction(
                                SessionsScreenAction.EditSessionClicked(
                                    session
                                )
                            )
                        },
                        shadowElevation = animateDpAsState(
                            if (dismissState.dismissDirection != null) 6.dp else 0.dp
                        ).value
                    ) { item ->
                        Column {
                            Text(item.name)
                            Text(item.lastAccessMillis.toDateTime())
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TagsScreenContentPreview() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(colorPickerColors) { background ->
            val session = Session(
                name = "background: ${background.brightness()}",
                lastAccessMillis = 333445666,
                color = background.toArgb()
            )
            SwipeableListItem(
                dismissDirection = DismissDirection.StartToEnd,
                onDismiss = { },
                dismissColor = Pink80
            ) { dismissState ->
                MyListItem(
                    modifier = Modifier
                        .border(1.dp, Color.LightGray),
                    colors = ListItemDefaults.colors(
                        leadingIconColor = Color(session.color).contrasted(),
                        headlineColor = Color(session.color).contrasted(),
                        trailingIconColor = Color(session.color).contrasted(),
                        containerColor = Color(session.color),
                    ),
                    item = session,
                    leadingIcon = R.drawable.document_and_ray,
                    onItemClick = { },
                    trailingIcon = R.drawable.baseline_edit_24,
                    onTrailingClick = {},
                    /*shadowElevation = animateDpAsState(
                        if (dismissState.dismissDirection != null) 6.dp else 0.dp
                    ).value*/
                ) { item ->
                    Column {
                        Text(item.name)
                        Text(item.lastAccessMillis.toDateTime())
                    }
                }
            }
        }
    }
}

private const val TAG = "SessionsScreenContent"
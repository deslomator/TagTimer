package com.deslomator.tagtimer.ui.main.sessions

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.SessionsTabAction
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.state.SessionsTabState
import com.deslomator.tagtimer.toDateTime
import com.deslomator.tagtimer.ui.MyListItem
import com.deslomator.tagtimer.ui.SwipeableListItem
import com.deslomator.tagtimer.ui.main.RootScreen
import com.deslomator.tagtimer.ui.main.trash.showSnackbar
import com.deslomator.tagtimer.ui.theme.Pink80
import com.deslomator.tagtimer.ui.theme.brightness
import com.deslomator.tagtimer.ui.theme.colorPickerColors
import com.deslomator.tagtimer.ui.theme.contrasted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionsTabContent(
    paddingValues: PaddingValues,
    outerNavHostController: NavHostController,
    state: SessionsTabState,
    onAction: (SessionsTabAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    BackHandler(enabled = state.showSessionDialog) {
        onAction(SessionsTabAction.DismissSessionDialog)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(6.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = state.sessions,
                key = { it.id }
            ) { session ->
                SwipeableListItem(
                    dismissDirection = DismissDirection.StartToEnd,
                    onDismiss = {
                        showSnackbar(
                            scope,
                            snackbarHostState,
                            context.getString(R.string.session_sent_to_trash)
                        )
                        onAction(SessionsTabAction.TrashSessionSwiped(session))
                    },
                    dismissColor = Pink80
                ) {
                    MyListItem(
                        modifier = Modifier
                            .clip(RoundedCornerShape(25.dp))
                            .border(1.dp, Color.LightGray, RoundedCornerShape(25.dp)),
                        colors = ListItemDefaults.colors(
                            leadingIconColor = Color(session.color).contrasted(),
                            headlineColor = Color(session.color).contrasted(),
                            trailingIconColor = Color(session.color).contrasted(),
                            containerColor = Color(session.color),
                        ),
                        item = session,
                        leadingIcon = R.drawable.document_and_ray,
                        onLeadingClick = {
                            outerNavHostController.navigate(
                                RootScreen.Active.routeWithArg(session.id)
                            )
                        },
                        onItemClick = {
                            outerNavHostController.navigate(
                                RootScreen.Active.routeWithArg(session.id)
                            )
                        },
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
    if (state.showSessionDialog) {
        SessionDialog(
            state = state,
            onAction = onAction,
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TagsScreenContentPreview() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(6.dp),
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
            ) {
                MyListItem(
                    modifier = Modifier
                        .clip(RoundedCornerShape(25.dp))
                        .border(1.dp, Color.LightGray, RoundedCornerShape(25.dp)),
                    colors = ListItemDefaults.colors(
                        leadingIconColor = Color(session.color).contrasted(),
                        headlineColor = Color(session.color).contrasted(),
                        trailingIconColor = Color(session.color).contrasted(),
                        containerColor = Color(session.color),
                    ),
                    item = session,
                    leadingIcon = R.drawable.document_and_ray,
                    onItemClick = { },
                    trailingIcon = R.drawable.edit,
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
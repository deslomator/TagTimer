package com.deslomator.tagtimer.ui.tags

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.SessionsScreenAction
import com.deslomator.tagtimer.state.SessionsScreenState
import com.deslomator.tagtimer.ui.SwipeableListItem
import com.deslomator.tagtimer.ui.theme.Pink80

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagsScreenContent(
    paddingValues: PaddingValues,
    outerNavHostController: NavHostController,
    state: SessionsScreenState,
    onAction: (SessionsScreenAction) -> Unit
) {
    if (state.showSessionDialog) {
        TagDialog(
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
                    TagListItem(
                        session = session,
                        onItemClick = { outerNavHostController.navigate("hello") },
                        trailingIcon = R.drawable.baseline_edit_24,
                        onTrailingClick = { onAction(SessionsScreenAction.EditSessionClicked(session)) },
                        shadowElevation = animateDpAsState(
                            if (dismissState.dismissDirection != null) 20.dp else 10.dp
                        ).value
                    )
                }
            }
        }
    }
}
package com.deslomator.tagtimer.ui.main.sessions

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.SessionsTabAction
import com.deslomator.tagtimer.state.SessionsTabState
import com.deslomator.tagtimer.ui.MyListItem
import com.deslomator.tagtimer.ui.theme.contrasted
import com.deslomator.tagtimer.util.toColor
import com.deslomator.tagtimer.util.toDateTime

@Composable
fun SessionsTabContent(
    paddingValues: PaddingValues,
    outerNavHostController: NavHostController,
    state: SessionsTabState,
    onAction: (SessionsTabAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()
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
                key = { it.id!! }
            ) { session ->
                MyListItem(
                    colors = CardDefaults.cardColors(
                        contentColor = session.color.toColor().contrasted(),
                        containerColor = session.color.toColor(),
                    ),
                    shape = RoundedCornerShape(25.dp),
                    border = BorderStroke(1.dp, Color.LightGray),
                    item = session,
                    leadingIcon = R.drawable.document_and_ray,
                    onLeadingClick = {
                        onAction(SessionsTabAction.ItemClicked(session))
                    },
                    onItemClick = {
                        outerNavHostController.navigate("active/${session.id}")
                    },
                    trailingIcon = R.drawable.edit,
                    onTrailingClick = {
                        onAction(SessionsTabAction.ItemClicked(session))
                    },
                ) { item ->
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(
                        modifier = Modifier.weight(1F)
                    ) {
                        Text(item.name)
                        Text(item.lastAccessMillis.toDateTime())
                    }
                    Column(
                        modifier = Modifier.padding(end = 5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(stringResource(R.string.events_count, item.eventCount))
                        if (item.running) {
                            val transition = rememberInfiniteTransition(label = "running_icon")
                            val alphaCycle by transition.animateFloat(
                                initialValue = .3F,
                                targetValue = 1F,
                                animationSpec = InfiniteRepeatableSpec(
                                    animation = tween(durationMillis = 1000),
                                    repeatMode = RepeatMode.Reverse
                                ),
                                label = "alpha_cycle"
                            )
                            Icon(
                                modifier = Modifier.graphicsLayer { alpha = alphaCycle },
                                painter = painterResource(R.drawable.running),
                                contentDescription = "running",
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }
    }
    if (state.showSessionDialog) {
        SessionDialog(
            state = state,
            onAction = onAction,
            scope = scope,
            snackbarHostState = snackbarHostState
        )
    }
}

private const val TAG = "SessionsScreenContent"
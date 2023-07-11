package com.deslomator.tagtimer.ui.main.sessions

import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.SessionsTabAction
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.state.SessionsTabState
import com.deslomator.tagtimer.ui.MyListItem
import com.deslomator.tagtimer.ui.SwipeableListItem
import com.deslomator.tagtimer.ui.theme.Pink80
import com.deslomator.tagtimer.ui.theme.brightness
import com.deslomator.tagtimer.ui.theme.colorPickerColors
import com.deslomator.tagtimer.ui.theme.contrasted
import com.deslomator.tagtimer.util.toDateTime
import com.deslomator.tagtimer.util.toElapsedTime
import kotlinx.coroutines.delay

@Composable
fun SessionsTabContent(
    paddingValues: PaddingValues,
    outerNavHostController: NavHostController,
    state: SessionsTabState,
    onAction: (SessionsTabAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    var time by rememberSaveable {
        mutableLongStateOf(System.currentTimeMillis())
    }
    if (state.sessions.any { it.startTimestampMillis > 0 }) {
        LaunchedEffect(Unit) {
            while (true) {
                delay(1000)
                time += 1000
            }
        }
    }
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
                        contentColor = Color(session.color).contrasted(),
                        containerColor = Color(session.color),
                    ),
                    shape = RoundedCornerShape(25.dp),
                    border = BorderStroke(1.dp, Color.LightGray),
                    item = session,
                    leadingIcon = R.drawable.document_and_ray,
                    onLeadingClick = {
                        onAction(SessionsTabAction.ItemClicked(session))
                    },
                    onItemClick = {
                        onAction(SessionsTabAction.ItemClicked(session))
                    },
                    trailingIcon = R.drawable.start,
                    onTrailingClick = {
                        outerNavHostController.navigate("active/${session.id}")
                    },
                ) { item ->
                    val elapsed = if (item.startTimestampMillis > 0) {
                        time - item.startTimestampMillis
                    } else {
                        item.durationMillis
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(
                        modifier = Modifier.weight(1F)
                    ) {
                        Text(item.name)
                        Text(item.lastAccessMillis.toDateTime())
                    }
                    Column(
                        modifier = Modifier.padding(end = 5.dp)
                    ) {
                        Text(stringResource(R.string.events_count, item.eventCount))
                        Text(elapsed.toElapsedTime())
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
package com.deslomator.tagtimer.ui.active.filter

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.state.ActiveSessionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterTopBar(
    state: ActiveSessionState,
    onBackClicked: () -> Unit,
    onShareFilteredEventsClick: () -> Unit,
    totalEvents: Int
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
        title = { Text(
            text = state.currentSession.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        ) },
        actions = {
            Text(
                modifier = Modifier.fillMaxWidth(.33F),
                text = stringResource(id = R.string.total_events, totalEvents),
                fontSize = 22.sp,
                textAlign = TextAlign.Center
            )
            IconButton(
                onClick = { onShareFilteredEventsClick() }
            ) {
                Icon(
                    painter = painterResource(R.drawable.share),
                    contentDescription = "share Events"
                )
            }
        }
    )
}

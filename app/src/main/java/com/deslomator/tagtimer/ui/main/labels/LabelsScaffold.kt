package com.deslomator.tagtimer.ui.main.labels

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.deslomator.tagtimer.action.LabelsTabAction
import com.deslomator.tagtimer.model.type.LabelScreen
import com.deslomator.tagtimer.state.LabelsTabState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LabelsScaffold(
    state: LabelsTabState,
    onAction: (LabelsTabAction) -> Unit,
    bottomBar: @Composable () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val pages = remember { listOf(LabelScreen.Tag, LabelScreen.Person, LabelScreen.Place) }
    val pagerState = rememberPagerState(initialPage = 1) { pages.size }
    Scaffold(
        topBar = {
            LabelsTopBar(
                onNewTagClick = { onAction(LabelsTabAction.AddNewTagClicked) },
                onNewPersonClick = { onAction(LabelsTabAction.AddNewPersonClicked) },
                onNewPlaceClick = { onAction(LabelsTabAction.AddNewPlaceClicked) },
                pagerState = pagerState,
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = { paddingValues ->
            LabelsTabContent(
                paddingValues = paddingValues,
                state = state,
                onAction = onAction,
                snackbarHostState = snackbarHostState,
                pagerState = pagerState,
                pages = pages
            )
        },
        bottomBar = bottomBar
    )
}


package com.deslomator.tagtimer.ui.main.labels

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Divider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.action.LabelsTabAction
import com.deslomator.tagtimer.model.type.Label
import com.deslomator.tagtimer.state.LabelsTabState
import com.deslomator.tagtimer.ui.main.labels.dialog.PersonDialog
import com.deslomator.tagtimer.ui.main.labels.dialog.PlaceDialog
import com.deslomator.tagtimer.ui.main.labels.dialog.TagDialog

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LabelsTabContent(
    paddingValues: PaddingValues,
    state: LabelsTabState,
    onAction: (LabelsTabAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()
    var currentPage by remember { mutableIntStateOf(0) }
    val pages = listOf(Label.Tag, Label.Person, Label.Place)
    val pagerState = rememberPagerState(initialPage = 0) { pages.size }
    BackHandler(
        enabled = state.showTagDialog
    ) {
        onAction(LabelsTabAction.DismissTagDialog)
    }
    BackHandler(
        enabled = state.showPersonDialog
    ) {
        onAction(LabelsTabAction.DismissPersonDialog)
    }
    BackHandler(
        enabled = state.showPlaceDialog
    ) {
        onAction(LabelsTabAction.DismissPlaceDialog)
    }
    LaunchedEffect(currentPage) {
//        Log.d(TAG, "navigating to page: $currentPage")
        pagerState.animateScrollToPage(currentPage)
    }
    LaunchedEffect(pagerState.targetPage) {
//        Log.d(TAG, "setting page: $currentPage")
        currentPage = pagerState.targetPage
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        Column {
            TabRow(
                selectedTabIndex = currentPage,
                divider = { Divider() }
            ) {
                pages.forEachIndexed { index, page ->
                    Tab(
                        selected = currentPage == index,
                        onClick = { currentPage = index }
                    ) {
                        Text(
                            modifier = Modifier.padding(bottom = 7.dp),
                            text = stringResource(id = page.stringId),
                            fontWeight = if (currentPage == index) FontWeight.Bold
                            else FontWeight.Normal
                        )
                    }
                }
            }
            HorizontalPager(
                modifier = Modifier.weight(1F),
                state = pagerState,
            ) { page ->
                when (pages[page]) {
                    Label.Tag -> {
                        TagLabel(state, onAction)
                    }
                    Label.Person -> {
                        PersonLabel (state, onAction)
                    }
                    Label.Place -> {
                        PlaceLabel(state, onAction)
                    }
                }
            }
        }
    }
    if (state.showTagDialog) {
        TagDialog(
            state = state,
            onAction = onAction,
            scope = scope,
            snackbarHostState = snackbarHostState
        )
    }
    if (state.showPersonDialog) {
        PersonDialog(
            state = state,
            onAction = onAction,
            scope = scope,
            snackbarHostState = snackbarHostState
        )
    }
    if (state.showPlaceDialog) {
        PlaceDialog(
            state = state,
            onAction = onAction,
            scope = scope,
            snackbarHostState = snackbarHostState
        )
    }
}

private const val TAG = "LabelsTabContent"
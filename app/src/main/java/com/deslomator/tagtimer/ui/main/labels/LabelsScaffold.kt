package com.deslomator.tagtimer.ui.main.labels

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.deslomator.tagtimer.action.LabelsTabAction
import com.deslomator.tagtimer.model.type.LabelType
import com.deslomator.tagtimer.state.LabelsTabState
import com.deslomator.tagtimer.ui.LabelsTopBar

@Composable
fun LabelsScaffold(
    state: LabelsTabState,
    onAction: (LabelsTabAction) -> Unit,
    bottomBar: @Composable () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val pages = remember { LabelType.entries }
    val pagerState = rememberPagerState(initialPage = 1) { pages.size }
    var showArchived by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        topBar = {
            LabelsTopBar(
                title = "",
                onBackClicked = null,
                pages = pages,
                currentPage = pagerState.currentPage,
                dialogState = state.dialogState,
                onAddLabelClick = { onAction(LabelsTabAction.AddNewLabelClicked(it)) },
                tagSort = state.tagSort,
                personSort = state.personSort,
                placeSort = state.placeSort,
                onTagSort = { onAction(LabelsTabAction.TagSortClicked(it)) },
                onPersonSort = { onAction(LabelsTabAction.PersonSortClicked(it)) },
                onPlaceSort = { onAction(LabelsTabAction.PlaceSortClicked(it)) },
                showArchived = showArchived,
                onShowArchivedChanged = { showArchived = it }
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
                pages = pages,
                showArchived = showArchived,
            )
        },
        bottomBar = bottomBar
    )
}


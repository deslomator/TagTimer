package com.deslomator.tagtimer.ui.active.selection

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.action.LabelSelectionAction
import com.deslomator.tagtimer.model.type.DialogState
import com.deslomator.tagtimer.model.type.LabelType
import com.deslomator.tagtimer.navigation.screen.MyTopScreens
import com.deslomator.tagtimer.state.LabelSelectionState
import com.deslomator.tagtimer.ui.LabelsTopBar
import com.deslomator.tagtimer.ui.active.TopNavigationBar

@Composable
fun LabelSelectionScaffold(
    navController: NavHostController,
    state: LabelSelectionState,
    onAction: (LabelSelectionAction) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val pages = LabelType.entries
    val pagerState = rememberPagerState(initialPage = 1) { pages.size }
    BackHandler(enabled = state.dialogState != DialogState.HIDDEN) {
        onAction(LabelSelectionAction.DismissLabelDialog)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            LabelsTopBar(
                pages = pages,
                currentPage = pagerState.currentPage,
                onAddLabelClick = { onAction(LabelSelectionAction.AddNewLabelClicked(it)) },
                dialogState = state.dialogState,
                tagSort = state.tagSort,
                personSort = state.personSort,
                placeSort = state.placeSort,
                onTagSort = { onAction(LabelSelectionAction.SortTagsClicked(it)) },
                onPersonSort = { onAction(LabelSelectionAction.SortPersonsClicked(it)) },
                onPlaceSort = { onAction(LabelSelectionAction.SortPlacesClicked(it)) },
                showArchived = state.showArchived,
                onShowArchivedChanged = {
                    onAction(
                        LabelSelectionAction.ShowArchivedClicked(
                            it
                        )
                    )
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            TopNavigationBar(
                sessionId = state.currentSession.id ?: 0L,
                navController = navController,
                selected = MyTopScreens.LABELS
            )
        }
    ) { paddingValues ->
        LabelSelectionContent(
            paddingValues = paddingValues,
            state = state,
            onAction = onAction,
            snackbarHostState = snackbarHostState,
            pagerState = pagerState,
            pages = pages,
            showArchived = state.showArchived,
        )
    }
}

private const val TAG = "ActiveSession"
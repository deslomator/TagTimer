package com.deslomator.tagtimer.ui.active.selection

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.action.LabelPreselectionAction
import com.deslomator.tagtimer.model.type.DialogState
import com.deslomator.tagtimer.model.type.LabelType
import com.deslomator.tagtimer.navigation.screen.MyTopScreens
import com.deslomator.tagtimer.state.LabelPreselectionState
import com.deslomator.tagtimer.ui.LabelsTopBar
import com.deslomator.tagtimer.ui.active.TopNavigationBar

@Composable
fun LabelSelectionScaffold(
    navController: NavHostController,
    state: LabelPreselectionState,
    onAction: (LabelPreselectionAction) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val pages = LabelType.entries
    val pagerState = rememberPagerState(initialPage = 1) { pages.size }
    BackHandler(enabled = state.dialogState != DialogState.HIDDEN) {
        onAction(LabelPreselectionAction.DismissLabelDialog)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column {
                TopNavigationBar(
                    sessionId = state.currentSession.id ?: 0L,
                    navController = navController,
                    selected = MyTopScreens.LABELS
                )
                LabelsTopBar(
                    pages = pages,
                    currentPage = pagerState.currentPage,
                    onAddLabelClick = { onAction(LabelPreselectionAction.AddNewLabelClicked(it)) },
                    dialogState = state.dialogState,
                    tagSort = state.tagSort,
                    personSort = state.personSort,
                    placeSort = state.placeSort,
                    onTagSort = { onAction(LabelPreselectionAction.SortTagsClicked(it)) },
                    onPersonSort = { onAction(LabelPreselectionAction.SortPersonsClicked(it)) },
                    onPlaceSort = { onAction(LabelPreselectionAction.SortPlacesClicked(it)) },
                    showArchived = state.showArchived,
                    onShowArchivedChanged = {
                        onAction(
                            LabelPreselectionAction.ShowArchivedClicked(
                                it
                            )
                        )
                    }
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
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
import com.deslomator.tagtimer.navigation.screen.BottomScreens
import com.deslomator.tagtimer.state.LabelSelectionState
import com.deslomator.tagtimer.ui.active.BottomNavigationBar

@Composable
fun LabelSelectionScaffold(
    sessionId: Long,
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
            LabelSelectionTopBar(
                labelType = pages[pagerState.currentPage],
                onAddLabelClick = { onAction(LabelSelectionAction.AddNewLabelClicked(it)) },
                dialogState = state.dialogState,
                tagSort = state.preferenceProvider.tagSort(),
                onTagSort = { onAction(LabelSelectionAction.SortTagsClicked(it)) },
                personSort = state.preferenceProvider.personSort(),
                onPersonSort = { onAction(LabelSelectionAction.SortPersonsClicked(it)) },
                placeSort = state.preferenceProvider.placeSort(),
                onPlaceSort = { onAction(LabelSelectionAction.SortPlacesClicked(it)) },
                showEnabled = state.preferenceProvider.showEnabledLabels(),
                onShowEnabledClick = { onAction(LabelSelectionAction.ShowEnabledClicked(it)) },
                showArchived = state.preferenceProvider.showArchivedLabels(),
                onShowArchivedClick = { onAction(LabelSelectionAction.ShowArchivedClicked(it)) },
                showTrashed = state.preferenceProvider.showTrashedLabels(),
                onShowTrashedClick = { onAction(LabelSelectionAction.ShowTrashedClicked(it)) },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            BottomNavigationBar(
                sessionId = sessionId,
                navController = navController,
                selected = BottomScreens.LABELS
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
        )
    }
}

private const val TAG = "ActiveSession"
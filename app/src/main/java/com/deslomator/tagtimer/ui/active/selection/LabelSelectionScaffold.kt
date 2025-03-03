package com.deslomator.tagtimer.ui.active.selection

import androidx.activity.compose.BackHandler
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.action.LabelPreselectionAction
import com.deslomator.tagtimer.model.type.DialogState
import com.deslomator.tagtimer.state.LabelPreselectionState
import com.deslomator.tagtimer.ui.LabelsTopBar

@Composable
fun LabelSelectionScaffold(
    navController: NavHostController,
    state: LabelPreselectionState,
    onAction: (LabelPreselectionAction) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var currentPage by remember { mutableIntStateOf(1) }
    BackHandler(enabled = state.dialogState != DialogState.HIDDEN) {
        onAction(LabelPreselectionAction.DismissLabelDialog)
    }
    Scaffold(
        topBar = {
            LabelsTopBar(
                title = state.currentSession.name,
                onBackClicked = {
                    if (
                        state.dialogState != DialogState.HIDDEN
                    )
                    {
                        onAction(LabelPreselectionAction.DismissLabelDialog)
                    } else {
                        navController.navigateUp()
                    }
                },
                currentPage = currentPage,
                onAddLabelClick = { onAction(LabelPreselectionAction.AddNewLabelClicked(it)) },
                dialogState = state.dialogState,
                tagSort = state.tagSort,
                personSort = state.personSort,
                placeSort = state.placeSort,
                onTagSort = { onAction(LabelPreselectionAction.SortTagsClicked(it)) },
                onPersonSort = { onAction(LabelPreselectionAction.SortPersonsClicked(it)) },
                onPlaceSort = { onAction(LabelPreselectionAction.SortPlacesClicked(it)) },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->
        LabelSelectionContent(
            paddingValues = paddingValues,
            state = state,
            onAction = onAction,
            onTabClick = { currentPage = it },
            snackbarHostState = snackbarHostState,
        )
    }
}

private const val TAG = "ActiveSession"
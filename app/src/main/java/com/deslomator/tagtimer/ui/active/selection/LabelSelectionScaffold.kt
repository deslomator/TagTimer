package com.deslomator.tagtimer.ui.active.selection

import android.util.Log
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
import com.deslomator.tagtimer.state.LabelPreselectionState

@Composable
fun LabelSelectionScaffold(
    navController: NavHostController,
    state: LabelPreselectionState,
    onAction: (LabelPreselectionAction) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var currentPage by remember { mutableIntStateOf(1) }
    BackHandler(
        enabled = state.showTagDialog || state.showPersonDialog || state.showPlaceDialog
    ) {
        onAction(LabelPreselectionAction.DismissTagDialog)
        onAction(LabelPreselectionAction.DismissPersonDialog)
        onAction(LabelPreselectionAction.DismissPlaceDialog)
    }
    Scaffold(
        topBar = {
            LabelSelectionTopBar(
                title = state.currentSession.name,
                onBackClicked = {
                    if (state.showTagDialog || state.showPersonDialog || state.showPlaceDialog) {
                        onAction(LabelPreselectionAction.DismissTagDialog)
                        onAction(LabelPreselectionAction.DismissPersonDialog)
                        onAction(LabelPreselectionAction.DismissPlaceDialog)
                    } else {
                        navController.navigateUp()
                    }
                },
                currentPage = currentPage,
                onAddTagClick = { onAction(LabelPreselectionAction.AddNewTagClicked) },
                onAddPersonClick = { onAction(LabelPreselectionAction.AddNewPersonClicked) },
                onAddPlaceClick = { onAction(LabelPreselectionAction.AddNewPlaceClicked) },
                showTagDialog = state.showTagDialog,
                showPersonDialog = state.showPersonDialog,
                showPlaceDialog = state.showPlaceDialog,
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
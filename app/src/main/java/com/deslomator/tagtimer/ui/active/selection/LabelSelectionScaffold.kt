package com.deslomator.tagtimer.ui.active.selection

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.LabelSelectionAction
import com.deslomator.tagtimer.model.type.DialogState
import com.deslomator.tagtimer.model.type.LabelType
import com.deslomator.tagtimer.navigation.screen.BottomScreens
import com.deslomator.tagtimer.state.LabelSelectionState
import com.deslomator.tagtimer.ui.active.BottomNavigationBar
import com.deslomator.tagtimer.ui.active.dialog.LabelDialog
import com.deslomator.tagtimer.ui.showSnackbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelSelectionScaffold(
    sessionId: Long,
    navController: NavHostController,
    state: LabelSelectionState,
    onAction: (LabelSelectionAction) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val pages = LabelType.entries
    val pagerState = rememberPagerState(initialPage = 1) { pages.size }
    BackHandler(enabled = state.dialogState != DialogState.HIDDEN) {
        onAction(LabelSelectionAction.DismissLabelDialog)
    }
    Box {
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
        AnimatedVisibility(
            visible = state.dialogState != DialogState.HIDDEN,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            val archivedMessage = stringResource(id = state.currentLabel.type.archiveMessageId)
            val unarchivedMessage = stringResource(id = state.currentLabel.type.unArchiveMessageId)
            val trashedMessage = stringResource(id = state.currentLabel.type.trashMessageId)
            val unTrashedMessage = stringResource(id = state.currentLabel.type.unTrashMessageId)
            val purgedMessage = stringResource(id = state.currentLabel.type.purgeMessageId)
            LabelDialog(
                currentLabel = state.currentLabel,
                onDismiss = { onAction(LabelSelectionAction.DismissLabelDialog) },
                onAccept = {
                    onAction(LabelSelectionAction.AcceptLabelEditionClicked(it))
                },
                dialogState = state.dialogState,
                canBeDeleted = state.canBeDeleted,
                onArchiveClicked = {
                    showSnackbar(
                        scope,
                        snackbarHostState,
                        archivedMessage
                    )
                    onAction(LabelSelectionAction.ArchiveLabelClicked)
                },
                onUnArchiveClicked = {
                    onAction(LabelSelectionAction.UnArchiveLabelClicked)
                    showSnackbar(
                        scope,
                        snackbarHostState,
                        unarchivedMessage
                    )
                },
                onTrashClicked = {
                    showSnackbar(
                        scope,
                        snackbarHostState,
                        trashedMessage
                    )
                    onAction(LabelSelectionAction.TrashLabelClicked)
                },
                onUnTrashClicked = {
                    showSnackbar(
                        scope,
                        snackbarHostState,
                        unTrashedMessage
                    )
                    onAction(LabelSelectionAction.UnTrashLabelClicked)
                },
                onPurgeClicked = {
                    showSnackbar(
                        scope,
                        snackbarHostState,
                        purgedMessage
                    )
                    onAction(LabelSelectionAction.PurgeLabelClicked)
                },
                title =
                    if (state.dialogState == DialogState.NEW_ITEM) state.currentLabel.type.newTitleId
                    else state.currentLabel.type.editTitleId,
                icon = state.currentLabel.type.iconId,
            )
        }
        if (state.showMessage) {
            BasicAlertDialog(
                onDismissRequest = {
                    onAction(LabelSelectionAction.DismissDeleteDialog)
                }
            ) {

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.secondary,
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(15.dp),
                        horizontalAlignment = Alignment.End
                        ) {
                        Text(
                            text = stringResource(id = R.string.cant_delete_label)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        TextButton(
                            onClick = { onAction(LabelSelectionAction.DismissDeleteDialog) }
                        ) {
                            Text(text = stringResource(id = R.string.accept))
                        }
                    }
                }
            }
        }
    }
}

private const val TAG = "ActiveSession"
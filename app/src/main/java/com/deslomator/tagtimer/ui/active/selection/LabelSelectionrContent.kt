package com.deslomator.tagtimer.ui.active.selection

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.LabelSelectionAction
import com.deslomator.tagtimer.model.type.DialogState
import com.deslomator.tagtimer.model.type.LabelType
import com.deslomator.tagtimer.state.LabelSelectionState
import com.deslomator.tagtimer.ui.EmptyListText
import com.deslomator.tagtimer.ui.TabIndicator
import com.deslomator.tagtimer.ui.active.dialog.LabelDialog
import com.deslomator.tagtimer.ui.showSnackbar
import kotlinx.coroutines.launch
import kotlin.enums.EnumEntries

@Composable
fun LabelSelectionContent(
    paddingValues: PaddingValues,
    state: LabelSelectionState,
    onAction: (LabelSelectionAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    pagerState: PagerState,
    pages: EnumEntries<LabelType>,
) {
    val scope = rememberCoroutineScope()
    BackHandler(
        enabled = state.dialogState != DialogState.HIDDEN
    ) {
        onAction(LabelSelectionAction.DismissLabelDialog)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        Column {
            TabRow(
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                containerColor = MaterialTheme.colorScheme.background,
                selectedTabIndex = pagerState.currentPage,
                divider = { },
                indicator = { tabPositions ->
                    TabIndicator(tabPositions = tabPositions, pagerState = pagerState)
                }
            ) {
                pages.forEachIndexed { index, page ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch { pagerState.animateScrollToPage(index) }
                        }
                    ) {
                        Text(
                            modifier = Modifier.padding(bottom = 7.dp, top = 7.dp),
                            text = stringResource(id = page.titleBarId),
                            fontWeight = if (pagerState.currentPage == index) FontWeight.Bold
                            else FontWeight.Normal,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            HorizontalDivider()
            HorizontalPager(
                modifier = Modifier.weight(1F),
                state = pagerState,
                beyondViewportPageCount = 1
            ) { page ->
                val current = remember(page) { pages[page] }
                val checkedMessage = stringResource(current.checkedStringId)
                val unCheckedMessage = stringResource(current.unCheckedStringId)
                LabelSelectionList(
                    labels = when (current) {
                        LabelType.TAG -> state.tags
                        LabelType.PERSON -> state.persons
                        LabelType.PLACE -> state.places
                    },
                    selected = when (current) {
                        LabelType.TAG -> state.selectedTags
                        LabelType.PERSON -> state.selectedPersons
                        LabelType.PLACE -> state.selectedPlaces
                    },
                    onItemClick = { tag, checked ->
                        showSnackbar(
                            scope,
                            snackbarHostState,
                            message = if (checked) checkedMessage else unCheckedMessage,
                        )
                        onAction(
                            LabelSelectionAction.SelectTagCheckedChange(tag,checked)
                        )
                    },
                    onLongClick = {
                        onAction(LabelSelectionAction.EditLabelClicked(it))
                    }
                )
            }
            EmptyListText(stringResource(id = R.string.tap_to_select_unselect))
        }
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
}

private const val TAG = "LabelSelectionContent"
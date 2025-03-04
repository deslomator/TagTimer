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
import com.deslomator.tagtimer.action.LabelPreselectionAction
import com.deslomator.tagtimer.model.type.DialogState
import com.deslomator.tagtimer.model.type.LabelArchiveState
import com.deslomator.tagtimer.model.type.LabelType
import com.deslomator.tagtimer.state.LabelPreselectionState
import com.deslomator.tagtimer.ui.EmptyListText
import com.deslomator.tagtimer.ui.LabelDialog
import com.deslomator.tagtimer.ui.TabIndicator
import com.deslomator.tagtimer.ui.showSnackbar
import kotlinx.coroutines.launch
import kotlin.enums.EnumEntries

@Composable
fun LabelSelectionContent(
    paddingValues: PaddingValues,
    state: LabelPreselectionState,
    onAction: (LabelPreselectionAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    pagerState: PagerState,
    pages: EnumEntries<LabelType>,
    showArchived: Boolean,
) {
    val scope = rememberCoroutineScope()
    BackHandler(
        enabled = state.dialogState != DialogState.HIDDEN
    ) {
        onAction(LabelPreselectionAction.DismissLabelDialog)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        Column {
            TabRow(
                containerColor = MaterialTheme.colorScheme.background,
                selectedTabIndex = pagerState.currentPage,
                divider = { HorizontalDivider() },
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
                        LabelType.TAG -> state.tags.filter { if(showArchived) true else !it.archived }
                        LabelType.PERSON -> state.persons.filter { if(showArchived) true else !it.archived }
                        LabelType.PLACE -> state.places.filter { if(showArchived) true else !it.archived }
                    },
                    preSelected = when (current) {
                        LabelType.TAG -> state.preSelectedTags
                        LabelType.PERSON -> state.preSelectedPersons
                        LabelType.PLACE -> state.preSelectedPlaces
                    },
                    onItemClick = { tag, checked ->
                        showSnackbar(
                            scope,
                            snackbarHostState,
                            message = if (checked) checkedMessage else unCheckedMessage,
                        )
                        onAction(
                            LabelPreselectionAction.SelectTagCheckedChange(tag,checked)
                        )
                    },
                    onLongClick = {
                        onAction(LabelPreselectionAction.EditLabelClicked(it))
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
        val message = stringResource(id = state.currentLabel.getLabelType().messageId)
        LabelDialog(
            currentLabel = state.currentLabel,
            onDismiss = { onAction(LabelPreselectionAction.DismissLabelDialog) },
            onAccept = {
                onAction(LabelPreselectionAction.AcceptLabelEditionClicked(it))
            },
            dialogState = state.dialogState,
            onTrash = {
                showSnackbar(
                    scope,
                    snackbarHostState,
                    message
                )
                onAction(LabelPreselectionAction.DeleteLabelClicked(state.currentLabel))
            },
            archiveState = when (state.dialogState) {
                DialogState.HIDDEN, DialogState.NEW_ITEM -> LabelArchiveState.HIDDEN
                else -> if (state.currentLabel.archived) LabelArchiveState.UNARCHIVE else LabelArchiveState.ARCHIVE
            },
            onArchiveClicked = { onAction(LabelPreselectionAction.ArchiveLabelClicked(it)) },
            title =
            if (
                state.dialogState == DialogState.EDIT_NO_DELETE ||
                state.dialogState == DialogState.EDIT_CAN_DELETE
            ) state.currentLabel.getLabelType().editTitleId
            else state.currentLabel.getLabelType().newTitleId,
            icon = state.currentLabel.getLabelType().iconId
        )
    }
}

private const val TAG = "LabelSelectionContent"
package com.deslomator.tagtimer.ui.main.labels

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Divider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.action.LabelsTabAction
import com.deslomator.tagtimer.model.type.LabelScreen
import com.deslomator.tagtimer.model.type.Sort
import com.deslomator.tagtimer.state.LabelsTabState
import com.deslomator.tagtimer.ui.TabIndicator
import com.deslomator.tagtimer.ui.main.labels.dialog.PersonDialog
import com.deslomator.tagtimer.ui.main.labels.dialog.PlaceDialog
import com.deslomator.tagtimer.ui.main.labels.dialog.TagDialog
import com.deslomator.tagtimer.ui.theme.hue
import com.deslomator.tagtimer.util.toColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LabelsTabContent(
    paddingValues: PaddingValues,
    state: LabelsTabState,
    onAction: (LabelsTabAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    pagerState: PagerState,
    pages: List<LabelScreen>
) {
    val scope = rememberCoroutineScope()
    val tags by remember(state.tagSort, state.tags) {
        derivedStateOf {
            state.tags.sortedWith(
                when (state.tagSort) {
                    Sort.COLOR -> compareBy { it.color.toColor().hue() }
                    Sort.NAME -> compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
                }
            )
        }
    }
    val persons by remember(state.personSort, state.persons) {
        derivedStateOf {
            state.persons.sortedWith(
                when (state.personSort) {
                    Sort.COLOR -> compareBy { it.color.toColor().hue() }
                    Sort.NAME -> compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
                }
            )
        }
    }
    val places by remember(state.placeSort, state.places) {
        derivedStateOf {
            state.places.sortedWith(
                when (state.placeSort) {
                    Sort.COLOR -> compareBy { it.color.toColor().hue() }
                    Sort.NAME -> compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
                }
            )
        }
    }
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        Column {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                divider = { Divider() },
                indicator = { tabPositions ->
                    TabIndicator(tabPositions = tabPositions, pagerState = pagerState)
                }
            ) {
                pages.forEachIndexed { index, page ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { scope.launch { pagerState.animateScrollToPage(index) } }
                    ) {
                        Text(
                            modifier = Modifier.padding(bottom = 7.dp),
                            text = stringResource(id = page.stringId),
                            fontWeight = if (pagerState.currentPage == index) FontWeight.Bold
                            else FontWeight.Normal
                        )
                    }
                }
            }
            HorizontalPager(
                modifier = Modifier.weight(1F),
                state = pagerState,
                beyondBoundsPageCount = 1
            ) { page ->
                when (pages[page]) {
                    LabelScreen.Tag -> {
                        LabelList(
                            labels = tags,
                            onItemClick = { onAction(LabelsTabAction.EditTagClicked(it)) }
                        )
                    }
                    LabelScreen.Person -> {
                        LabelList(
                            labels = persons,
                            onItemClick = { onAction(LabelsTabAction.EditPersonClicked(it)) }
                        )
                    }
                    LabelScreen.Place -> {
                        LabelList(
                            labels = places,
                            onItemClick = { onAction(LabelsTabAction.EditPlaceClicked(it)) }
                        )
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
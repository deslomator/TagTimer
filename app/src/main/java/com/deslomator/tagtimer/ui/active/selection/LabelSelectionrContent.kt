package com.deslomator.tagtimer.ui.active.selection

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Divider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.LabelPreselectionAction
import com.deslomator.tagtimer.model.type.LabelScreen
import com.deslomator.tagtimer.model.type.Sort
import com.deslomator.tagtimer.state.LabelPreselectionState
import com.deslomator.tagtimer.state.SharedState
import com.deslomator.tagtimer.ui.TabIndicator
import com.deslomator.tagtimer.ui.active.dialog.PersonDialog
import com.deslomator.tagtimer.ui.active.dialog.PlaceDialog
import com.deslomator.tagtimer.ui.active.dialog.TagDialog
import com.deslomator.tagtimer.ui.showSnackbar
import com.deslomator.tagtimer.ui.theme.hue
import com.deslomator.tagtimer.util.toColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LabelSelectionContent(
    paddingValues: PaddingValues,
    state: LabelPreselectionState,
    sharedState: SharedState,
    onAction: (LabelPreselectionAction) -> Unit,
    onTabClick: (Int) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    val scope = rememberCoroutineScope()
    val pages = remember { listOf(LabelScreen.Tag, LabelScreen.Person, LabelScreen.Place) }
    val pagerState = rememberPagerState(initialPage = 1) { pages.size }
    val tags by remember(sharedState.tagSort, state.tags) {
        derivedStateOf {
            state.tags.sortedWith(
                when (sharedState.tagSort) {
                    Sort.COLOR -> compareBy { it.color.toColor().hue() }
                    Sort.NAME -> compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
                }
            )
        }
    }
    val persons by remember(sharedState.personSort, state.persons) {
        derivedStateOf {
            state.persons.sortedWith(
                when (sharedState.personSort) {
                    Sort.COLOR -> compareBy { it.color.toColor().hue() }
                    Sort.NAME -> compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
                }
            )
        }
    }
    val places by remember(sharedState.placeSort, state.places) {
        derivedStateOf {
            state.places.sortedWith(
                when (sharedState.placeSort) {
                    Sort.COLOR -> compareBy { it.color.toColor().hue() }
                    Sort.NAME -> compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
                }
            )
        }
    }
    LaunchedEffect(pagerState.currentPage) { onTabClick(pagerState.currentPage) }
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
                        onClick = {
                            scope.launch { pagerState.animateScrollToPage(index) }
                        }
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
                        val checkedMessage = stringResource(R.string.tag_checked)
                        val unCheckedMessage = stringResource(R.string.tag_unchecked)
                        LabelSelectionList(
                            labels = tags,
                            preSelected = state.preSelectedTags,
                            onCheckedChange = { tag, checked ->
                                showSnackbar(
                                    scope,
                                    snackbarHostState,
                                    message = if (checked) checkedMessage else unCheckedMessage,
                                )
                                onAction(LabelPreselectionAction.SelectTagCheckedChange(tag, checked))
                            },
                            onLongClick = {
                                onAction(LabelPreselectionAction.EditTagClicked(it))
                            }
                        )
                    }
                    LabelScreen.Person -> {
                        val checkedMessage = stringResource(R.string.person_checked)
                        val unCheckedMessage = stringResource(R.string.person_unchecked)
                        LabelSelectionList(
                            labels = persons,
                            preSelected = state.preSelectedPersons,
                            onCheckedChange = { person, checked ->
                                showSnackbar(
                                    scope,
                                    snackbarHostState,
                                    message = if (checked) checkedMessage else unCheckedMessage,
                                )
                                onAction(LabelPreselectionAction.SelectPersonCheckedChange(person, checked))
                            },
                            onLongClick = {
                                onAction(LabelPreselectionAction.EditPersonClicked(it))
                            }
                        )
                    }
                    LabelScreen.Place -> {
                        val checkedMessage = stringResource(R.string.place_checked)
                        val unCheckedMessage = stringResource(R.string.place_unchecked)
                        LabelSelectionList(
                            labels = places,
                            preSelected = state.preSelectedPlaces,
                            onCheckedChange = { place, checked ->
                                showSnackbar(
                                    scope,
                                    snackbarHostState,
                                    message = if (checked) checkedMessage else unCheckedMessage,
                                )
                                onAction(LabelPreselectionAction.SelectPlaceCheckedChange(place, checked))
                            },
                            onLongClick = {
                                onAction(LabelPreselectionAction.EditPlaceClicked(it))
                            }
                        )
                    }
                }
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.long_press_to_edit_remove),
                textAlign = TextAlign.Center,
            )
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

private const val TAG = "LabelSelectionContent"
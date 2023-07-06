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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.LabelPreselectionAction
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.type.LabelScreen
import com.deslomator.tagtimer.model.type.Sort
import com.deslomator.tagtimer.state.LabelPreselectionState
import com.deslomator.tagtimer.state.SharedState
import com.deslomator.tagtimer.ui.active.dialog.PersonDialog
import com.deslomator.tagtimer.ui.active.dialog.PlaceDialog
import com.deslomator.tagtimer.ui.active.dialog.TagDialog
import com.deslomator.tagtimer.ui.showSnackbar
import com.deslomator.tagtimer.ui.theme.hue

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
    var currentPage by remember { mutableIntStateOf(1) }
    val pages = listOf(LabelScreen.Tag, LabelScreen.Person, LabelScreen.Place)
    val pagerState = rememberPagerState(initialPage = 1) { pages.size }
    LaunchedEffect(currentPage) {
//        Log.d(TAG, "target page changed")
        pagerState.animateScrollToPage(currentPage)
    }
    LaunchedEffect(pagerState.targetPage) {
        currentPage = pagerState.targetPage
        onTabClick(currentPage)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        Column {
            TabRow(
                selectedTabIndex = currentPage,
                divider = { Divider() }
            ) {
                pages.forEachIndexed { index, page ->
                    Tab(
                        selected = currentPage == index,
                        onClick = {
                            currentPage = index
                        }
                    ) {
                        Text(
                            modifier = Modifier.padding(bottom = 7.dp),
                            text = stringResource(id = page.stringId),
                            fontWeight = if (currentPage == index) FontWeight.Bold
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
                        LaunchedEffect(Unit) {
                        }
                        LabelSelectionList(
                            labels = when (sharedState.tagSort) {
                                Sort.NAME -> state.tags.sortedBy { it.name }
                                Sort.COLOR -> state.tags.sortedBy { Color(it.color).hue() }
                            },
                            preSelected = state.preSelectedTags,
                            onCheckedChange = { id, checked ->
                                showSnackbar(
                                    scope,
                                    snackbarHostState,
                                    message = if (checked) checkedMessage else unCheckedMessage,
                                )
                                onAction(LabelPreselectionAction.SelectTagCheckedChange(id, checked))
                            },
                            onLongClick = {
                                onAction(LabelPreselectionAction.EditTagClicked(it as Label.Tag))
                            }
                        )
                    }
                    LabelScreen.Person -> {
                        val checkedMessage = stringResource(R.string.person_checked)
                        val unCheckedMessage = stringResource(R.string.person_unchecked)
                        LaunchedEffect(Unit) {
                        }
                        LabelSelectionList(
                            labels = when (sharedState.personSort) {
                                Sort.NAME -> state.persons.sortedBy { it.name }
                                Sort.COLOR -> state.persons.sortedBy { Color(it.color).hue() }
                            },
                            preSelected = state.preSelectedPersons,
                            onCheckedChange = { id, checked ->
                                showSnackbar(
                                    scope,
                                    snackbarHostState,
                                    message = if (checked) checkedMessage else unCheckedMessage,
                                )
                                onAction(LabelPreselectionAction.SelectPersonCheckedChange(id, checked))
                            },
                            onLongClick = {
                                onAction(LabelPreselectionAction.EditPersonClicked(it as Label.Person))
                            }
                        )
                    }
                    LabelScreen.Place -> {
                        val checkedMessage = stringResource(R.string.place_checked)
                        val unCheckedMessage = stringResource(R.string.place_unchecked)
                        LaunchedEffect(Unit) {
                        }
                        LabelSelectionList(
                            labels = when (sharedState.placeSort) {
                                Sort.NAME -> state.places.sortedBy { it.name }
                                Sort.COLOR -> state.places.sortedBy { Color(it.color).hue() }
                            },
                            preSelected = state.preSelectedPlaces,
                            onCheckedChange = { id, checked ->
                                showSnackbar(
                                    scope,
                                    snackbarHostState,
                                    message = if (checked) checkedMessage else unCheckedMessage,
                                )
                                onAction(LabelPreselectionAction.SelectPlaceCheckedChange(id, checked))
                            },
                            onLongClick = {
                                onAction(LabelPreselectionAction.EditPlaceClicked(it as Label.Place))
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
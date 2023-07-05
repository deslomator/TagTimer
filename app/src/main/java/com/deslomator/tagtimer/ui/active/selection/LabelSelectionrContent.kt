package com.deslomator.tagtimer.ui.active.selection

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.LabelPreselectionAction
import com.deslomator.tagtimer.model.type.LabelScreen
import com.deslomator.tagtimer.state.LabelPreselectionState
import com.deslomator.tagtimer.ui.active.dialog.PersonDialog
import com.deslomator.tagtimer.ui.active.dialog.PlaceDialog
import com.deslomator.tagtimer.ui.active.dialog.TagDialog
import com.deslomator.tagtimer.ui.showSnackbar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LabelSelectionContent(
    paddingValues: PaddingValues,
    state: LabelPreselectionState,
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
                            labels = state.tags,
                            preSelected = state.preSelectedTags,
                            onCheckedChange = { id, checked ->
                                showSnackbar(
                                    scope,
                                    snackbarHostState,
                                    message = if (checked) checkedMessage else unCheckedMessage,
                                )
                                onAction(LabelPreselectionAction.SelectTagCheckedChange(id, checked))
                            }
                        )
                    }
                    LabelScreen.Person -> {
                        val checkedMessage = stringResource(R.string.person_checked)
                        val unCheckedMessage = stringResource(R.string.person_unchecked)
                        LaunchedEffect(Unit) {
                        }
                        LabelSelectionList(
                            labels = state.persons,
                            preSelected = state.preSelectedPersons,
                            onCheckedChange = { id, checked ->
                                showSnackbar(
                                    scope,
                                    snackbarHostState,
                                    message = if (checked) checkedMessage else unCheckedMessage,
                                )
                                onAction(LabelPreselectionAction.SelectPersonCheckedChange(id, checked))
                            }
                        )
                    }
                    LabelScreen.Place -> {
                        val checkedMessage = stringResource(R.string.place_checked)
                        val unCheckedMessage = stringResource(R.string.place_unchecked)
                        LaunchedEffect(Unit) {
                        }
                        LabelSelectionList(
                            labels = state.places,
                            preSelected = state.preSelectedPlaces,
                            onCheckedChange = { id, checked ->
                                showSnackbar(
                                    scope,
                                    snackbarHostState,
                                    message = if (checked) checkedMessage else unCheckedMessage,
                                )
                                onAction(LabelPreselectionAction.SelectPlaceCheckedChange(id, checked))
                            }
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

private const val TAG = "LabelSelectionContent"
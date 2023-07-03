package com.deslomator.tagtimer.ui.active.selection

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Divider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.action.LabelPreselectionAction
import com.deslomator.tagtimer.model.type.LabelScreen
import com.deslomator.tagtimer.state.LabelPreselectionState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LabelSelectionContent(
    paddingValues: PaddingValues,
    state: LabelPreselectionState,
    onAction: (LabelPreselectionAction) -> Unit,
) {
    var currentPage by remember { mutableIntStateOf(1) }
    val pages = listOf(LabelScreen.Tag, LabelScreen.Person, LabelScreen.Place)
    val pagerState = rememberPagerState(initialPage = 1) { pages.size }
    LaunchedEffect(currentPage) {
        pagerState.animateScrollToPage(currentPage)
    }
    LaunchedEffect(pagerState.targetPage) {
        currentPage = pagerState.targetPage
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
                        onClick = { currentPage = index }
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
                beyondBoundsPageCount = 0
            ) { page ->
                when (pages[page]) {
                    LabelScreen.Tag -> {
                        LabelSelectionList(
                            labels = state.tags,
                            preSelected = state.preSelectedTags.map { it.labelId },
                            onCheckedChange = { id, checked ->
                                onAction(LabelPreselectionAction.SelectTagCheckedChange(id, checked))
                            }
                        )
                    }
                    LabelScreen.Person -> {
                        LabelSelectionList(
                            labels = state.persons,
                            preSelected = state.preSelectedPersons.map { it.labelId },
                            onCheckedChange = { id, checked ->
                                onAction(LabelPreselectionAction.SelectPersonCheckedChange(id, checked))
                            }
                        )
                    }
                    LabelScreen.Place -> {
                        LabelSelectionList(
                            labels = state.places,
                            preSelected = state.preSelectedPlaces.map { it.labelId },
                            onCheckedChange = { id, checked ->
                                onAction(LabelPreselectionAction.SelectPlaceCheckedChange(id, checked))
                            }
                        )
                    }
                }
            }
        }
    }
}

private const val TAG = "LabelSelectionContent"
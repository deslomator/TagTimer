package com.deslomator.tagtimer.ui.main.trash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.TrashTabAction
import com.deslomator.tagtimer.model.type.Trash
import com.deslomator.tagtimer.state.TrashTabState
import com.deslomator.tagtimer.ui.TabIndicator
import com.deslomator.tagtimer.ui.showSnackbar
import kotlinx.coroutines.launch

@Composable
fun TrashTabContent(
    paddingValues: PaddingValues,
    state: TrashTabState,
    onAction: (TrashTabAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val pages = remember { listOf(Trash.Session, Trash.Tag, Trash.Person, Trash.Place) }
    val pagerState = rememberPagerState(initialPage = 1) { pages.size }
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
                        onClick = { scope.launch { pagerState.animateScrollToPage(index) } }
                    ) {
                        Text(
                            modifier = Modifier.padding(bottom = 7.dp, top = 7.dp),
                            text = stringResource(id = page.stringId),
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
                when (pages[page]) {
                    Trash.Session -> {
                        SessionTrash(state, onAction, scope, snackbarHostState, context)
                    }
                    Trash.Tag -> {
                        LabelTrash(
                            items = state.tags,
                            onLeadingClick = {
                                onAction(TrashTabAction.RestoreTagClicked(it))
                                showSnackbar(
                                    scope,
                                    snackbarHostState,
                                    context.getString(R.string.tag_restored)
                                )
                            },
                            onTrailingClick = {
                                onAction(
                                    TrashTabAction.DeleteTagClicked(it))
                                showSnackbar(
                                    scope,
                                    snackbarHostState,
                                    context.getString(R.string.tag_deleted)
                                )
                            },
                        )
                    }
                    Trash.Person -> {
                        LabelTrash(
                            items = state.persons,
                            onLeadingClick = {
                                onAction(TrashTabAction.RestorePersonClicked(it))
                                showSnackbar(
                                    scope,
                                    snackbarHostState,
                                    context.getString(R.string.person_restored)
                                )
                            },
                            onTrailingClick = {
                                onAction(
                                    TrashTabAction.DeletePersonClicked(it))
                                showSnackbar(
                                    scope,
                                    snackbarHostState,
                                    context.getString(R.string.person_deleted)
                                )
                            },
                        )
                    }
                    Trash.Place -> {
                        LabelTrash(
                            items = state.places,
                            onLeadingClick = {
                                onAction(TrashTabAction.RestorePlaceClicked(it))
                                showSnackbar(
                                    scope,
                                    snackbarHostState,
                                    context.getString(R.string.place_restored)
                                )
                            },
                            onTrailingClick = {
                                onAction(
                                    TrashTabAction.DeletePlaceClicked(it))
                                showSnackbar(
                                    scope,
                                    snackbarHostState,
                                    context.getString(R.string.place_deleted)
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}



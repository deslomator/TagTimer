package com.deslomator.tagtimer.ui.main.labels

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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.LabelsTabAction
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.type.LabelScreen
import com.deslomator.tagtimer.model.type.LabelSort
import com.deslomator.tagtimer.state.LabelsTabState
import com.deslomator.tagtimer.ui.LabelDialog
import com.deslomator.tagtimer.ui.TabIndicator
import com.deslomator.tagtimer.ui.showSnackbar
import com.deslomator.tagtimer.ui.theme.hue
import com.deslomator.tagtimer.util.toColor
import kotlinx.coroutines.launch

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
                    LabelSort.COLOR -> compareBy { it.color.toColor().hue() }
                    LabelSort.NAME -> compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
                }
            )
        }
    }
    val persons by remember(state.personSort, state.persons) {
        derivedStateOf {
            state.persons.sortedWith(
                when (state.personSort) {
                    LabelSort.COLOR -> compareBy { it.color.toColor().hue() }
                    LabelSort.NAME -> compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
                }
            )
        }
    }
    val places by remember(state.placeSort, state.places) {
        derivedStateOf {
            state.places.sortedWith(
                when (state.placeSort) {
                    LabelSort.COLOR -> compareBy { it.color.toColor().hue() }
                    LabelSort.NAME -> compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
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
    AnimatedVisibility(
        state.showTagDialog,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        val message = stringResource(id = R.string.tag_sent_to_trash)
        LabelDialog(
            currentLabel = state.currentTag,
            onDismiss = { onAction(LabelsTabAction.DismissTagDialog) },
            onAccept = { name, color ->
                val t = Label.Tag(
                    name = name,
                    color = color
                )
                onAction(LabelsTabAction.AcceptTagEditionClicked(t))
            },
            showTrash = state.isEditingTag,
            onTrash = {
                showSnackbar(
                    scope,
                    snackbarHostState,
                    message
                )
                onAction(LabelsTabAction.DeleteTagClicked(state.currentTag))
            },
            title = if(state.isEditingTag) R.string.edit_tag else R.string.new_tag,
            icon = R.drawable.tag
        )
    }
    AnimatedVisibility(
        state.showPersonDialog,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        val message = stringResource(id = R.string.person_sent_to_trash)
        LabelDialog(
            currentLabel = state.currentPerson,
            onDismiss = { onAction(LabelsTabAction.DismissPersonDialog) },
            onAccept = { name, color ->
                val t = Label.Person(
                    name = name,
                    color = color
                )
                onAction(LabelsTabAction.AcceptPersonEditionClicked(t))
            },
            showTrash = state.isEditingPerson,
            onTrash = {
                showSnackbar(
                    scope,
                    snackbarHostState,
                    message
                )
                onAction(LabelsTabAction.DeletePersonClicked(state.currentPerson))
            },
            title = if(state.isEditingPerson) R.string.edit_person else R.string.new_person,
            icon = R.drawable.person
        )
    }
    AnimatedVisibility(
        state.showPlaceDialog,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        val message = stringResource(id = R.string.place_sent_to_trash)
        LabelDialog(
            currentLabel = state.currentPlace,
            onDismiss = { onAction(LabelsTabAction.DismissPlaceDialog) },
            onAccept = { name, color ->
                val t = Label.Place(
                    name = name,
                    color = color
                )
                onAction(LabelsTabAction.AcceptPlaceEditionClicked(t))
            },
            showTrash = state.isEditingPlace,
            onTrash = {
                showSnackbar(
                    scope,
                    snackbarHostState,
                    message
                )
                onAction(LabelsTabAction.DeletePlaceClicked(state.currentPlace))
            },
            title = if(state.isEditingPlace) R.string.edit_place else R.string.new_place,
            icon = R.drawable.place
        )
    }
}

private const val TAG = "LabelsTabContent"
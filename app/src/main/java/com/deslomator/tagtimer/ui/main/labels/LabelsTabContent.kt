package com.deslomator.tagtimer.ui.main.labels

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.LabelsTabAction
import com.deslomator.tagtimer.model.Tag
import com.deslomator.tagtimer.model.type.Label
import com.deslomator.tagtimer.state.LabelsTabState
import com.deslomator.tagtimer.ui.MyListItem
import com.deslomator.tagtimer.ui.SwipeableListItem
import com.deslomator.tagtimer.ui.main.labels.dialog.PersonDialog
import com.deslomator.tagtimer.ui.main.labels.dialog.PlaceDialog
import com.deslomator.tagtimer.ui.main.labels.dialog.TagDialog
import com.deslomator.tagtimer.ui.theme.Pink80
import com.deslomator.tagtimer.ui.theme.brightness
import com.deslomator.tagtimer.ui.theme.colorPickerColors
import com.deslomator.tagtimer.ui.theme.contrasted

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LabelsTabContent(
    paddingValues: PaddingValues,
    state: LabelsTabState,
    onAction: (LabelsTabAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var currentPage by remember { mutableIntStateOf(0) }
    val pages = listOf(Label.Tag, Label.Person, Label.Place)
    val pagerState = rememberPagerState(initialPage = 0) { pages.size }
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
    LaunchedEffect(currentPage) {
//        Log.d(TAG, "navigating to page: $currentPage")
        pagerState.animateScrollToPage(currentPage)
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
                userScrollEnabled = false
            ) { page ->
                when (pages[page]) {
                    Label.Tag -> {
                        TagLabel(state, scope, snackbarHostState, context, onAction)
                    }
                    Label.Person -> {
                        PersonLabel (state, scope, snackbarHostState, context, onAction)
                    }
                    Label.Place -> {
                        PlaceLabel(state, scope, snackbarHostState, context, onAction)
                    }
                }
            }
        }
    }
    if (state.showTagDialog) {
        TagDialog(
            state = state,
            onAction = onAction,
            tag = state.currentTag
        )
    }
    if (state.showPersonDialog) {
        PersonDialog(
            state = state,
            onAction = onAction,
            person = state.currentPerson
        )
    }
    if (state.showPlaceDialog) {
        PlaceDialog(
            state = state,
            onAction = onAction,
            place = state.currentPlace
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun LabelsTabContentPreview() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(colorPickerColors) { background ->
            val tag = Tag(
                color = background.toArgb(),
                label = "brightness: ${background.brightness()}",
            )
            SwipeableListItem(
                dismissDirection = DismissDirection.StartToEnd,
                onDismiss = { },
                dismissColor = Pink80
            ) { dismissState ->
                MyListItem(
                    modifier = Modifier
                        .border(1.dp, Color.LightGray, CutCornerShape(topStart = 20.dp))
                        .clip(CutCornerShape(topStart = 20.dp)),
                    colors = CardDefaults.cardColors(
                        contentColor = Color(tag.color).contrasted(),
                        containerColor = Color(tag.color),
                    ),
                    item = tag,
                    onItemClick = {  },
                    trailingIcon = R.drawable.edit,
                    onTrailingClick = {  },
//                    shadowElevation = animateDpAsState(
//                        if (dismissState.dismissDirection != null) 20.dp else 10.dp
//                    ).value
                ) { item ->
                    Text(item.label)
                }
            }
        }
    }
}

private const val TAG = "LabelsTabContent"
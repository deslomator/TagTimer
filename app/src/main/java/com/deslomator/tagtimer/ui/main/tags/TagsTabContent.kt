package com.deslomator.tagtimer.ui.main.tags

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.TagsTabAction
import com.deslomator.tagtimer.model.Tag
import com.deslomator.tagtimer.state.TagsTabState
import com.deslomator.tagtimer.ui.MyListItem
import com.deslomator.tagtimer.ui.SwipeableListItem
import com.deslomator.tagtimer.ui.main.trash.showSnackbar
import com.deslomator.tagtimer.ui.theme.Pink80
import com.deslomator.tagtimer.ui.theme.brightness
import com.deslomator.tagtimer.ui.theme.colorPickerColors
import com.deslomator.tagtimer.ui.theme.contrasted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagsTabContent(
    paddingValues: PaddingValues,
    state: TagsTabState,
    onAction: (TagsTabAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    BackHandler(
        enabled = state.showTagDialog
    ) {
        onAction(TagsTabAction.DismissTagDialog)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(6.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = state.tags,
                key = { it.id }
            ) { tag ->
                SwipeableListItem(
                    dismissDirection = DismissDirection.StartToEnd,
                    onDismiss = {
                        showSnackbar(
                            scope,
                            snackbarHostState,
                            context.getString(R.string.tag_sent_to_trash)
                        )
                        onAction(TagsTabAction.TrashTagSwiped(tag))
                                },
                    dismissColor = Pink80
                ) {
                    MyListItem(
                        modifier = Modifier
                            .clip(CutCornerShape(topStart = 20.dp))
                            .border(1.dp, Color.LightGray, CutCornerShape(topStart = 20.dp)),
                        leadingIcon = R.drawable.tag,
                        onLeadingClick = { onAction(TagsTabAction.EditTagClicked(tag)) },
                        colors = ListItemDefaults.colors(
                            leadingIconColor = Color(tag.color).contrasted(),
                            headlineColor = Color(tag.color).contrasted(),
                            trailingIconColor = Color(tag.color).contrasted(),
                            containerColor = Color(tag.color),
                        ),
                        item = tag,
                        onItemClick = { onAction(TagsTabAction.EditTagClicked(tag)) },
                    ) { item ->
                        Column {
                            Text(item.label)
                            Text(item.category)
                        }
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TagsScreenContentPreview() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(colorPickerColors) { background ->
            val tag = Tag(
                color = background.toArgb(),
                label = "brightness: ${background.brightness()}",
                category = "asfa sfasf asfasf ddd444"
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
                    colors = ListItemDefaults.colors(
                        leadingIconColor = Color(tag.color).contrasted(),
                        headlineColor = Color(tag.color).contrasted(),
                        trailingIconColor = Color(tag.color).contrasted(),
                        containerColor = Color(tag.color),
                    ),
                    item = tag,
                    onItemClick = {  },
                    trailingIcon = R.drawable.edit,
                    onTrailingClick = {  },
                    shadowElevation = animateDpAsState(
                        if (dismissState.dismissDirection != null) 20.dp else 10.dp
                    ).value
                ) { item ->
                    Column {
                        Text(item.label)
                        Text(item.category)
                    }
                }
            }
        }
    }
}
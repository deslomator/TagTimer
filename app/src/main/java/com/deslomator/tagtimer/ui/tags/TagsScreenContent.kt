package com.deslomator.tagtimer.ui.tags

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.TagsScreenAction
import com.deslomator.tagtimer.state.TagsScreenState
import com.deslomator.tagtimer.ui.SwipeableListItem
import com.deslomator.tagtimer.ui.theme.Pink80
import com.deslomator.tagtimer.ui.theme.SoftGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagsScreenContent(
    paddingValues: PaddingValues,
    outerNavHostController: NavHostController,
    state: TagsScreenState,
    onAction: (TagsScreenAction) -> Unit
) {
    if (state.showTagDialog) {
        TagDialog(
            state = state,
            onAction = onAction,
            tag = state.currentTag
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = state.tags,
                key = { it.id }
            ) { tag ->
                SwipeableListItem(
                    dismissDirection = DismissDirection.StartToEnd,
                    onDismiss = { onAction(TagsScreenAction.TrashTagSwiped(tag)) },
                    dismissColor = Pink80
                ) { dismissState ->
                    TagListItem(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp)),
                        colors = ListItemDefaults.colors(
                            containerColor = Color(tag.color)
                        ),
                        tag = tag,
                        onItemClick = { outerNavHostController.navigate("hello") },
                        trailingIcon = R.drawable.baseline_edit_24,
                        onTrailingClick = { onAction(TagsScreenAction.EditTagClicked(tag)) },
                        shadowElevation = animateDpAsState(
                            if (dismissState.dismissDirection != null) 20.dp else 10.dp
                        ).value
                    )
                }
            }
        }
    }
}
package com.deslomator.tagtimer.ui.active

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.ui.MyListItem
import com.deslomator.tagtimer.ui.main.trash.showSnackbar
import com.deslomator.tagtimer.ui.theme.contrasted

@Composable
fun PreSelectedTagsList(
    modifier: Modifier,
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    if (state.preSelectedTags.isEmpty()) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.tap_toolbar_icon_add_pst),
            textAlign = TextAlign.Center
        )
    }
    LazyVerticalGrid(
        modifier = modifier,
        contentPadding = PaddingValues(6.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        columns = GridCells.Adaptive(minSize = 150.dp)
    ) {
        items(
            items = state.tags.filter { tag ->
                state.preSelectedTags.map { it.tagId }.contains(tag.id)
            },
            key = { it.id }
        ) { tag ->
            MyListItem(
                modifier = Modifier
                    .clip(CutCornerShape(topStart = 20.dp))
                    .border(1.dp, Color.LightGray, CutCornerShape(topStart = 20.dp)),
                colors = ListItemDefaults.colors(
                    headlineColor = Color(tag.color).contrasted(),
                    containerColor = Color(tag.color),
                ),
                item = tag,
                onItemClick = {
                    if (!state.isRunning) {
                        showSnackbar(
                            scope,
                            snackbarHostState,
                            context.getString(R.string.tap_play_to_add_event)
                        )
                    }
                    onAction(ActiveSessionAction.PreSelectedTagClicked(tag))
                },
            ) { item ->
                Text(item.label)
            }
        }
    }
}
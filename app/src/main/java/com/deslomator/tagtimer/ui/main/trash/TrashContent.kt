package com.deslomator.tagtimer.ui.main.trash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.SessionsTrashAction
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.Trash
import com.deslomator.tagtimer.state.SessionsTrashState
import com.deslomator.tagtimer.toDateTime
import com.deslomator.tagtimer.ui.MyListItem
import com.deslomator.tagtimer.ui.theme.brightness
import com.deslomator.tagtimer.ui.theme.colorPickerColors
import com.deslomator.tagtimer.ui.theme.contrasted

@Composable
fun TrashContent(
    paddingValues: PaddingValues,
    state: SessionsTrashState,
    onAction: (SessionsTrashAction) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        AnimatedVisibility(
            visible = state.currentTrash == Trash.SESSION,
            enter = fadeIn(
                animationSpec = tween(500, 0, LinearEasing)
            ),
            exit = fadeOut(
                animationSpec = tween(500, 0, LinearEasing)
            )
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(6.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = state.sessions,
                    key = { it.id }
                ) { session ->
                    MyListItem(
                        modifier = Modifier
                            .clip(RoundedCornerShape(25.dp))
                            .border(1.dp, Color.LightGray, RoundedCornerShape(25.dp)),
                        colors = ListItemDefaults.colors(
                            leadingIconColor = Color(session.color).contrasted(),
                            headlineColor = Color(session.color).contrasted(),
                            trailingIconColor = Color(session.color).contrasted(),
                            containerColor = Color(session.color),
                        ),
                        item = session,
                        leadingIcon = R.drawable.restore_from_trash,
                        onLeadingClick = {
                            onAction(
                                SessionsTrashAction.RestoreSessionClicked(
                                    session
                                )
                            )
                        },
                        trailingIcon = R.drawable.delete_forever,
                        onTrailingClick = {
                            onAction(
                                SessionsTrashAction.DeleteSessionClicked(
                                    session
                                )
                            )
                        },
                    ) { item ->
                        Column {
                            Text(item.name)
                            Text(item.lastAccessMillis.toDateTime())
                        }
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = state.currentTrash == Trash.TAG,
            enter = fadeIn(
                animationSpec = tween(500, 0, LinearEasing)
            ),
            exit = fadeOut(
                animationSpec = tween(500, 0, LinearEasing)
            )
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
                    MyListItem(
                        modifier = Modifier
                            .clip(CutCornerShape(topStart = 20.dp))
                            .border(1.dp, Color.LightGray, CutCornerShape(topStart = 20.dp)),
                        colors = ListItemDefaults.colors(
                            leadingIconColor = Color(tag.color).contrasted(),
                            headlineColor = Color(tag.color).contrasted(),
                            trailingIconColor = Color(tag.color).contrasted(),
                            containerColor = Color(tag.color),
                        ),
                        item = tag,
                        leadingIcon = R.drawable.restore_from_trash,
                        onLeadingClick = {
                            onAction(
                                SessionsTrashAction.RestoreTagClicked(
                                    tag
                                )
                            )
                        },
                        trailingIcon = R.drawable.delete_forever,
                        onTrailingClick = {
                            onAction(
                                SessionsTrashAction.DeleteTagClicked(
                                    tag
                                )
                            )
                        },
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
}

@Composable
@Preview
fun TrashContentPreview() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(colorPickerColors) { background ->
            val session = Session(
                name = "background: ${background.brightness()}",
                lastAccessMillis = 333445666,
                color = background.toArgb()
            )
            MyListItem(
                modifier = Modifier
                    .clip(RoundedCornerShape(25.dp))
                    .border(1.dp, Color.LightGray, RoundedCornerShape(25.dp)),
                colors = ListItemDefaults.colors(
                    leadingIconColor = Color(session.color).contrasted(),
                    headlineColor = Color(session.color).contrasted(),
                    trailingIconColor = Color(session.color).contrasted(),
                    containerColor = Color(session.color),
                ),
                item = session,
                leadingIcon = R.drawable.restore_from_trash,
                onLeadingClick = {  },
                trailingIcon = R.drawable.delete_forever,
                onTrailingClick = {  },
                shadowElevation = 0.dp
            ) { item ->
                Column {
                    Text(item.name)
                    Text(item.lastAccessMillis.toDateTime())
                }
            }
        }
    }
}
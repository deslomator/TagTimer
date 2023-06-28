package com.deslomator.tagtimer.ui.main.trash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.TrashTabAction
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.Trash
import com.deslomator.tagtimer.state.TrashTabState
import com.deslomator.tagtimer.toDateTime
import com.deslomator.tagtimer.toElapsedTime
import com.deslomator.tagtimer.ui.MyListItem
import com.deslomator.tagtimer.ui.theme.brightness
import com.deslomator.tagtimer.ui.theme.colorPickerColors
import com.deslomator.tagtimer.ui.theme.contrasted
import kotlinx.coroutines.CoroutineScope
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
                        colors = CardDefaults.cardColors(
                            contentColor = Color(session.color).contrasted(),
                            containerColor = Color(session.color),
                        ),
                        shape = RoundedCornerShape(25.dp),
                        border = BorderStroke(1.dp, Color.LightGray),
                        item = session,
                        leadingIcon = R.drawable.restore_from_trash,
                        onLeadingClick = {
                            onAction(
                                TrashTabAction.RestoreSessionClicked(
                                    session
                                )
                            )
                            showSnackbar(
                                scope,
                                snackbarHostState,
                                context.getString(R.string.session_restored)
                            )
                        },
                        trailingIcon = R.drawable.delete_forever,
                        onTrailingClick = {
                            onAction(
                                TrashTabAction.DeleteSessionClicked(
                                    session
                                )
                            )
                            showSnackbar(
                                scope,
                                snackbarHostState,
                                context.getString(R.string.session_deleted)
                            )
                        },
                    ) { item ->
                        Column(
                            modifier = Modifier.weight(1F)
                        ) {
                            Text(item.name)
                            Text(item.lastAccessMillis.toDateTime())
                        }
                        Column(
                            modifier = Modifier.padding(end = 5.dp)
                        ) {
                            Text(stringResource(R.string.events, item.eventCount))
                            Text(item.durationMillis.toElapsedTime())
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
                        colors = CardDefaults.cardColors(
                            contentColor = Color(tag.color).contrasted(),
                            containerColor = Color(tag.color),
                        ),
                        item = tag,
                        leadingIcon = R.drawable.restore_from_trash,
                        onLeadingClick = {
                            onAction(
                                TrashTabAction.RestoreTagClicked(
                                    tag
                                )
                            )
                            showSnackbar(
                                scope,
                                snackbarHostState,
                                context.getString(R.string.tag_restored)
                            )
                        },
                        trailingIcon = R.drawable.delete_forever,
                        onTrailingClick = {
                            onAction(
                                TrashTabAction.DeleteTagClicked(
                                    tag
                                )
                            )
                            showSnackbar(
                                scope,
                                snackbarHostState,
                                context.getString(R.string.tag_deleted)
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

fun showSnackbar(
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    message: String
) {
    scope.launch {
        snackbarHostState.currentSnackbarData?.dismiss()
        snackbarHostState.showSnackbar(
            message = message,
            duration = SnackbarDuration.Short
        )
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
                colors = CardDefaults.cardColors(
                    contentColor = Color(session.color).contrasted(),
                    containerColor = Color(session.color),
                ),
                item = session,
                leadingIcon = R.drawable.restore_from_trash,
                onLeadingClick = {  },
                trailingIcon = R.drawable.delete_forever,
                onTrailingClick = {  },
            ) { item ->
                Column {
                    Text(item.name)
                    Text(item.lastAccessMillis.toDateTime())
                }
            }
        }
    }
}
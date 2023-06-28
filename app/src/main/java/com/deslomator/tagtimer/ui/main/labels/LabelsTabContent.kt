package com.deslomator.tagtimer.ui.main.labels

import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.deslomator.tagtimer.action.LabelsTabAction
import com.deslomator.tagtimer.model.Label
import com.deslomator.tagtimer.model.Tag
import com.deslomator.tagtimer.state.LabelsTabState
import com.deslomator.tagtimer.ui.MyListItem
import com.deslomator.tagtimer.ui.SwipeableListItem
import com.deslomator.tagtimer.ui.theme.Pink80
import com.deslomator.tagtimer.ui.theme.brightness
import com.deslomator.tagtimer.ui.theme.colorPickerColors
import com.deslomator.tagtimer.ui.theme.contrasted

@Composable
fun LabelsTabContent(
    paddingValues: PaddingValues,
    state: LabelsTabState,
    onAction: (LabelsTabAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    BackHandler(
        enabled = state.showTagDialog
    ) {
        onAction(LabelsTabAction.DismissTagDialog)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        AnimatedVisibility(
            visible = state.currentLabel == Label.TAG,
            enter = fadeIn(
                animationSpec = tween(250, 0, LinearEasing)
            ),
            exit = fadeOut(
                animationSpec = tween(250, 0, LinearEasing)
            )
        ) {
            TagLabel(state, scope, snackbarHostState, context, onAction)
        }
        AnimatedVisibility(
            visible = state.currentLabel == Label.PERSON,
            enter = fadeIn(
                animationSpec = tween(250, 0, LinearEasing)
            ),
            exit = fadeOut(
                animationSpec = tween(250, 0, LinearEasing)
            )
        ) {
            PersonLabel(state, scope, snackbarHostState, context, onAction)
        }
        AnimatedVisibility(
            visible = state.currentLabel == Label.PLACE,
            enter = fadeIn(
                animationSpec = tween(250, 0, LinearEasing)
            ),
            exit = fadeOut(
                animationSpec = tween(250, 0, LinearEasing)
            )
        ) {
            PlaceLabel(state, scope, snackbarHostState, context, onAction)
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
                    Column {
                        Text(item.label)
                        Text(item.category)
                    }
                }
            }
        }
    }
}
package com.deslomator.tagtimer.ui.active.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.model.type.Label
import com.deslomator.tagtimer.state.ActiveSessionState
import com.deslomator.tagtimer.ui.MyDialog
import com.deslomator.tagtimer.ui.active.PersonSelectionList
import com.deslomator.tagtimer.ui.active.PlaceSelectionList
import com.deslomator.tagtimer.ui.active.TagSelectionList
import com.deslomator.tagtimer.ui.main.TopButton

@Composable
fun LabelSelectionDialog(
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit,
) {
    MyDialog(
        onDismiss = { onAction(ActiveSessionAction.DismissTagDialog) },
        onAccept = { onAction(ActiveSessionAction.DismissTagDialog) },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.width(15.dp))
            TopButton(
                modifier = Modifier.weight(1F),
                type = Label.TAG,
                currentType = state.currentLabel,
                onTypeClick = { onAction(ActiveSessionAction.LabelTypeSelected(it)) },
                text = R.string.tags
            )
            Spacer(modifier = Modifier.width(15.dp))
            TopButton(
                modifier = Modifier.weight(1F),
                type = Label.PERSON,
                currentType = state.currentLabel,
                onTypeClick = { onAction(ActiveSessionAction.LabelTypeSelected(it)) },
                text = R.string.persons
            )
            Spacer(modifier = Modifier.width(15.dp))
            TopButton(
                modifier = Modifier.weight(1F),
                type = Label.PLACE,
                currentType = state.currentLabel,
                onTypeClick = { onAction(ActiveSessionAction.LabelTypeSelected(it)) },
                text = R.string.places
            )
            Spacer(modifier = Modifier.width(15.dp))
        }
        AnimatedVisibility(
            visible = state.currentLabel == Label.TAG,
            enter = fadeIn(
                animationSpec = tween(250, 0, LinearEasing)
            ),
            exit = fadeOut(
                animationSpec = tween(250, 0, LinearEasing)
            )
        ) {
            TagSelectionList(state, onAction)
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
            PersonSelectionList(state, onAction)
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
            PlaceSelectionList(state, onAction)
        }
    }
}

private const val TAG = "LabelSelectionDialog"
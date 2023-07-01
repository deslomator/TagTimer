package com.deslomator.tagtimer.ui.main.sessions

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.SessionsTabAction
import com.deslomator.tagtimer.state.SessionsTabState
import com.deslomator.tagtimer.ui.ColorPicker
import com.deslomator.tagtimer.ui.DialogTextField
import com.deslomator.tagtimer.ui.MyDialog
import com.deslomator.tagtimer.ui.showSnackbar
import kotlinx.coroutines.CoroutineScope

@Composable
fun SessionDialog(
    state: SessionsTabState,
    onAction: (SessionsTabAction) -> Unit,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    val message = stringResource(id = R.string.session_sent_to_trash)
    var name by rememberSaveable {
        mutableStateOf(state.currentSession.name)
    }
    var notes by rememberSaveable {
        mutableStateOf(state.currentSession.notes)
    }
    var color by rememberSaveable {
        mutableIntStateOf(state.currentSession.color)
    }
    MyDialog(
        onDismiss = {
            onAction(SessionsTabAction.DismissSessionDialog)
        },
        onAccept = {
            val s = state.currentSession.copy(
                lastAccessMillis = System.currentTimeMillis(),
                name = name,
                notes = notes,
                color = color
            )
            onAction(SessionsTabAction.DialogAcceptClicked(s))
        },
        showTrash = state.isEditingSession,
        onTrash = {
            showSnackbar(
                scope,
                snackbarHostState,
                message
            )
            onAction(SessionsTabAction.TrashSessionSwiped(state.currentSession))
        },
        title = if (state.isEditingSession) R.string.edit_session else R.string.new_session
    ) {
        DialogTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = R.string.name,
            icon = R.drawable.document_and_ray
        )
        DialogTextField(
            value = notes,
            onValueChange = { notes = it },
            placeholder = R.string.type_a_note,
            icon = R.drawable.note
        )
        Spacer(modifier = Modifier.height(7.dp))
        ColorPicker(
            selectedColor = Color(color),
            onItemClick = { color = it }
        )
    }
}

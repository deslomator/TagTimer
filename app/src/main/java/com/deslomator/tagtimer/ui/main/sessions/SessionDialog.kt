package com.deslomator.tagtimer.ui.main.sessions

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
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
import com.deslomator.tagtimer.util.toDate
import com.deslomator.tagtimer.util.toTime
import kotlinx.coroutines.CoroutineScope
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionDialog(
    state: SessionsTabState,
    onAction: (SessionsTabAction) -> Unit,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    var show by rememberSaveable {
        mutableStateOf(Show.DIALOG)
    }
    var sessionDate by rememberSaveable {
        mutableLongStateOf(state.currentSession.sessionDateMillis)
    }
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
    val copy = stringResource(id = R.string.copy)
    MyDialog(
        onDismiss = {
            onAction(SessionsTabAction.DismissSessionDialog)
        },
        onAccept = {
            val s = state.currentSession.copy(
                sessionDateMillis = sessionDate,
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
            onAction(SessionsTabAction.TrashSessionClicked)
        },
        showCopy = state.isEditingSession,
        onCopy = {
            onAction(SessionsTabAction.CopySessionClicked(copy))
        },
        title = if (state.isEditingSession) R.string.edit_session else R.string.new_session
    ) {
        AnimatedContent(targetState = show) { s ->
            when (s) {
                Show.DIALOG -> {
                    Column {
                        if (state.currentSession.sessionDateMillis > 0) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                TextButton(
                                    onClick = { show = Show.DATE },
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Text(text = sessionDate.toDate())
                                }
                                TextButton(
                                    onClick = { show = Show.TIME },
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Text(text = sessionDate.toTime())
                                }
                            }
                        }
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
                Show.DATE -> {
                    val pickerState = rememberDatePickerState(
                        initialSelectedDateMillis = sessionDate
                    )
                    DatePickerDialog(
                        onDismissRequest = { show = Show.DIALOG },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    val picked = pickerState.selectedDateMillis ?: sessionDate
                                    val hoursOnly = sessionDate % (24 * 3600 * 1000)
                                    sessionDate = picked + hoursOnly
                                    show = Show.DIALOG
                                }
                            ) {
                                Text(text = stringResource(id = R.string.accept))
                            }
                        },
                    ) {
                        DatePicker(state = pickerState)
                    }
                }
                Show.TIME -> {
                    val tzOffset = TimeZone.getDefault().getOffset(sessionDate)
                    val seconds = ((sessionDate + tzOffset) / 1000)
                    val hour = (seconds % (3600 * 24)) / 3600
                    val minute = (seconds % 3600) / 60
                    val pickerState = rememberTimePickerState(
                        initialHour = hour.toInt(),
                        initialMinute = minute.toInt(),
                        is24Hour = true
                    )
                    TimePickerDialog(
                        title = stringResource(id = R.string.select_time),
                        onCancel = { show = Show.DIALOG },
                        onConfirm = {
                            val hoursOnly = sessionDate % (24 * 3600 * 1000)
                            val daysOnly = sessionDate - hoursOnly
                            val h = pickerState.hour * 3600 * 1000L
                            val m = pickerState.minute * 60 * 1000L
                            val hm = h + m - tzOffset
                            sessionDate = daysOnly + hm
                            show = Show.DIALOG
                        }
                    ) {
                        TimePicker(state = pickerState)
                    }
                }
            }
        }
    }
}

private const val TAG = "SessionDialog"

private enum class Show { DIALOG, DATE, TIME }


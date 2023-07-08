package com.deslomator.tagtimer.ui.backup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.BackupAction
import com.deslomator.tagtimer.state.BackupState
import com.deslomator.tagtimer.state.Result
import com.deslomator.tagtimer.ui.showSnackbar

@Composable
fun BackupContent(
    paddingValues: PaddingValues,
    state: BackupState,
    onAction: (BackupAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { onAction(BackupAction.FullBackupClicked) }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(stringResource(id = R.string.full_backup))
                        Icon(
                            painter = painterResource(id = R.drawable.document_and_ray),
                            contentDescription = null
                        )
                    }
                }
                Button(
                    onClick = { onAction(BackupAction.BackupLabelsClicked) }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(stringResource(id = R.string.backup_labels))
                        Row {
                            Icon(
                                painter = painterResource(id = R.drawable.tag),
                                contentDescription = null
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.person),
                                contentDescription = null
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.place),
                                contentDescription = null
                            )
                        }
                    }
                }
            }
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(6.dp)
            ) {
                items(
                    items = state.files,
                    key = { it.absolutePath }
                ) { file ->
                    FileItem(
                        file = file,
                        onDeleteClick = {
                            showSnackbar(
                                scope,
                                snackbarHostState,
                                context.getString(R.string.backup_deleted)
                            )
                            onAction(BackupAction.DeleteBackupClicked(file))
                        },
                        onShareClick = { onAction(BackupAction.ShareBackupClicked(file)) },
                        onRestoreClick = { onAction(BackupAction.RestoreBackupClicked(file)) },
                    )
                }
            }
            Divider()
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                text = stringResource(id = R.string.tap_file_for_actions),
                textAlign = TextAlign.Center
            )
        }
    }
    LaunchedEffect(state.showSnackBar) {
        if (state.showSnackBar) {
            snackbarHostState.currentSnackbarData?.dismiss()
            val result = snackbarHostState.showSnackbar(
                message = when (state.result) {
                    Result.BAD_FILE -> context.getString(R.string.bad_backup_file)
                    Result.RESTORED -> context.getString(R.string.backup_restored)
                    Result.DELETED -> context.getString(R.string.backup_deleted)
                    Result.BACKED -> context.getString(R.string.backup_saved)
                    Result.BACKUP_FAILED -> context.getString(R.string.backup_failed)
                    Result.NOTHING_TO_BACKUP -> context.getString(R.string.nothing_to_backup)
                    Result.RESTORE_FAILED -> context.getString(R.string.restore_failed)
                    Result.NOTHING_TO_RESTORE -> context.getString(R.string.nothing_to_restore)
                },
                duration = SnackbarDuration.Short
            )
        }
        onAction(BackupAction.SnackbarShown)
    }
}

private const val TAG = "BackupContent"
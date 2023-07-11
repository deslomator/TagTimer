package com.deslomator.tagtimer.ui.backup

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.BackupAction
import com.deslomator.tagtimer.state.BackupState
import com.deslomator.tagtimer.ui.showSnackbar

@Composable
fun BackupContent(
    paddingValues: PaddingValues,
    state: BackupState,
    onAction: (BackupAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    context: Context
) {
    val scope = rememberCoroutineScope()
    var result by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")) { uri ->
        result = uri
    }
    result?.let {
        onAction(BackupAction.UriReceived(it))
        result = null
    }
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
                        onSaveClick = {
                            onAction(BackupAction.SaveBackupClicked(file))
                            launcher.launch(file.name)
                        },
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
            val res = snackbarHostState.showSnackbar(
                message = context.getString(state.result.stringId),
                duration = SnackbarDuration.Short
            )
        }
        onAction(BackupAction.SnackbarShown)
    }
}

private const val TAG = "BackupContent"
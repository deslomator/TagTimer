package com.deslomator.tagtimer.ui.backup

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.BackupAction
import com.deslomator.tagtimer.model.type.BackupButton
import com.deslomator.tagtimer.state.BackupState
import com.deslomator.tagtimer.ui.EmptyListText
import com.deslomator.tagtimer.ui.showSnackbar
import java.io.File

@Composable
fun BackupContent(
    paddingValues: PaddingValues,
    state: BackupState,
    onAction: (BackupAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    context: Context
) {
    val scope = rememberCoroutineScope()
    var showSnackbar by rememberSaveable { mutableStateOf(false) }

    var saveToStorageActivityResult by remember { mutableStateOf<Uri?>(null) }
    val saveToStorageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        saveToStorageActivityResult = uri
    }
    saveToStorageActivityResult?.let {
        onAction(BackupAction.SaveToStorageUriReceived(it))
        saveToStorageActivityResult = null
    }

    var loadFromStorageActivityResult by remember { mutableStateOf<Uri?>(null) }
    val loadFromStorageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        loadFromStorageActivityResult = uri
        if (uri == null) onAction(BackupAction.LoadFromStorageDialogDismissed)
        showSnackbar = true
    }
    loadFromStorageActivityResult?.let { storageUri ->
        val tempFile = File(
            context.cacheDir,
            "tempFile"
        )
        onAction(BackupAction.LoadFromStorageUriReceived(storageUri, tempFile))
        loadFromStorageActivityResult = null
        showSnackbar = true
    }
    LaunchedEffect(state.loadFileFromStorage) {
        if (state.loadFileFromStorage)
            loadFromStorageLauncher.launch(arrayOf("application/json"))
    }

    LaunchedEffect(state.result, showSnackbar) {
        if (showSnackbar) {
            val r = showSnackbar(
                scope = scope,
                snackbarHostState = snackbarHostState,
                message = context.getString(state.result.stringId)
            )
        }
        showSnackbar = false
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                BackupButton.entries.forEach { button ->
                    Button(
                        modifier = Modifier
                            .widthIn(max = 127.dp)
                            .fillMaxHeight(),
                        onClick = {
                            showSnackbar = button.showSnackBar
                            onAction(BackupAction.TopButtonClicked(button))
                        }) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(id = button.textId),
                                    textAlign = TextAlign.Center
                                )
                            }
                            Spacer(Modifier.size(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                button.iconIds.forEach { iconId ->
                                    Icon(
                                        painter = painterResource(id = iconId),
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.size(15.dp))
            HorizontalDivider()
            Spacer(Modifier.size(5.dp))
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(6.dp)
            ) {
                items(items = state.files, key = { it.absolutePath }) { file ->
                    FileItem(
                        file = file,
                        onDeleteClick = {
                            showSnackbar(
                                scope, snackbarHostState, context.getString(R.string.backup_deleted)
                            )
                            onAction(BackupAction.DeleteBackupClicked(file))
                        },
                        onShareClick = { onAction(BackupAction.ShareBackupClicked(file)) },
                        onRestoreClick = {
                            showSnackbar = true
                            onAction(BackupAction.RestoreBackupClicked(file))
                        },
                        onSaveClick = {
                            onAction(BackupAction.SaveBackupToStorageClicked(file))
                            saveToStorageLauncher.launch(file.name)
                        },
                    )
                }
            }
            HorizontalDivider()
            EmptyListText(text = stringResource(id = R.string.tap_file_for_actions))
        }
    }
}

private const val TAG = "BackupContent"
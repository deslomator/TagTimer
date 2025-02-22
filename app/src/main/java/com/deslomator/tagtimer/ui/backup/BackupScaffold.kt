package com.deslomator.tagtimer.ui.backup

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.action.BackupAction
import com.deslomator.tagtimer.model.type.Shared
import com.deslomator.tagtimer.state.BackupState
import com.deslomator.tagtimer.ui.active.ShareData

@Composable
fun BackupScaffold(
    navController: NavHostController,
    state: BackupState,
    onAction: (BackupAction) -> Unit,
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    if (state.shareFile && state.currentFile != null && state.currentString.isNotEmpty()) {
        ShareData(
            context = context,
            fileName = state.currentFile.nameWithoutExtension,
            data = state.currentString,
            onDataShared = { onAction(BackupAction.BackupShared) },
            type = Shared.Json
        )
    }
    Scaffold(
        topBar = {
            BackupTopBar(
                onBackClicked = {
                    navController.navigate("root") {
                        popUpTo("root") {
                            inclusive = false
                        }
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->
        BackupContent(
            paddingValues = paddingValues,
            state = state,
            onAction = onAction,
            snackbarHostState = snackbarHostState,
            context = context
        )
    }
}

private const val TAG = "BackupScaffold"
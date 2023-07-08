package com.deslomator.tagtimer.ui.backup

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.action.BackupAction
import com.deslomator.tagtimer.state.BackupState

@Composable
fun BackupScaffold(
    navController: NavHostController,
    state: BackupState,
    onAction: (BackupAction) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
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
            snackbarHostState = snackbarHostState
        )
    }
}

private const val TAG = "BackupScaffold"
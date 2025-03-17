package com.deslomator.tagtimer.ui.backup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.BackupAction
import com.deslomator.tagtimer.navigation.screen.SessionsTabScreen
import com.deslomator.tagtimer.state.BackupState
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupScaffold(
    navController: NavHostController,
    state: BackupState,
    onAction: (BackupAction) -> Unit,
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                BackupTopBar(
                    onBackClicked = {
                        navController.navigate(SessionsTabScreen) {
                            popUpTo(SessionsTabScreen) {
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
        if (state.showFullRestoreDialog) {
            BasicAlertDialog(
                onDismissRequest = {
                    onAction(BackupAction.FullRestoreDismissed)
                }
            ) {
                val warning = stringResource(R.string.warning_this_will_erase)
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.secondary,
                    )
                ) {
                    Column(modifier = Modifier.padding(15.dp)) {
                        Text(
                            text = stringResource(id = R.string.import_data),
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(
                                id = R.string.load_backup,
                                state.currentFile?.name ?: "no name"
                            ),
                        )
                        Spacer(modifier = Modifier.height(25.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = warning,
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = {
                                    onAction(BackupAction.FullRestoreDismissed)
                                }
                            ) {
                                Text(text = stringResource(id = R.string.cancel))
                            }
                            TextButton(
                                onClick = {
                                    onAction(BackupAction.FullRestoreAccepted)
                                }
                            ) {
                                Text(text = stringResource(id = R.string.accept))
                            }
                        }
                    }
                }
            }
        }
    }
}

private const val TAG = "BackupScaffold"

@Preview
@Composable
fun BackupScaffoldPreview() {
    val s = BackupState(
        files = listOf(File("dir/file1"), File("file2"), File("file3"))
    )
    BackupScaffold(
        navController = NavHostController(LocalContext.current),
        state = s,
        onAction = {}
    )
}
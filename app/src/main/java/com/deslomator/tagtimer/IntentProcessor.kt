package com.deslomator.tagtimer

import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.util.Consumer
import androidx.documentfile.provider.DocumentFile
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.type.Result
import com.deslomator.tagtimer.util.restoreBackup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntentProcessor(
    intent: Intent,
    activity: ComponentActivity,
    appDao: AppDao
) {
    var result by rememberSaveable { mutableStateOf(Result.RESTORE_FAILED) }
    /*
      showImportDialog survives configuration changes,
      only interacting with the dialog makes it false.
      It is true on cold boot: it the app was launched by
      choosing a .json file from other app it will be ready.
      Receiving a newIntent sets it to true again
      */
    var showImportDialog by rememberSaveable { mutableStateOf(true) }
    /*
    intentState receives the intent on cold boot, once the
    dialog is dismissed or the backup completed it's set to null
    and only OnNewIntent sets it tho a different value.
    It survives configuration changes.
     */
    var intentState: Intent? by rememberSaveable { mutableStateOf(intent) }
    /*
    loadBackup is false on every configuration change as it should be
     */
    var loadBackup by remember { mutableStateOf(false) }
    /*
    MainActivity is SingleInstance, so it always receives the launch
    intent in onCreate, and successive ones in OnNewIntent.
    This DisposableEffect is the simplest way to get them.
     */
    DisposableEffect(Unit) {
        val listener = Consumer<Intent> {
            intentState = it
            showImportDialog = true
        }
        activity.addOnNewIntentListener(listener)
        onDispose { activity.removeOnNewIntentListener(listener) }
    }
    /*
    AlertDialog needs both showImportDialog && intentState
    or it would show on every cold launch regardless or
    a valid intent being present.
     */
    if (showImportDialog && (intentState?.data != null)) {
        AlertDialog(
            onDismissRequest = {
                showImportDialog = false
                intentState = null
                loadBackup = false
            }
        ) {
            var filename by remember { mutableStateOf("") }
            val documentFile = DocumentFile.fromSingleUri(activity, intentState!!.data!!)
            filename = documentFile?.name.toString()
            Card {
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
                            filename
                        ),
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.warning_this_will_erase),
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {
                                showImportDialog = false
                                intentState = null
                                loadBackup = false
                            }
                        ) {
                            Text(text = stringResource(id = R.string.cancel))
                        }
                        TextButton(
                            onClick = {
                                showImportDialog = false
                                loadBackup = true
                            }
                        ) {
                            Text(text = stringResource(id = R.string.accept))
                        }
                    }
                }
            }
        }
    }
    if (loadBackup) {
        LaunchedEffect(Unit) {
            intentState?.data?.let { intentUri ->
                Log.d(TAG, "inside intentState?.data?.let")
                runBlocking {
                    withContext(Dispatchers.IO) {
                        try {
                            activity.contentResolver.openInputStream(intentUri).use {
                                it?.readBytes()?.decodeToString()
                            }?.let {
                                result = restoreBackup(appDao, it)
                            }
                            Log.d(TAG, "loadBackup: $result")
                        } catch (error: Error) {
                            Log.e(TAG, "loadBackup: $error")
                        }
                    }
                }
            }
            Log.i(TAG, "Loading backup finished, result: ${result.name}")
            intentState = null
            loadBackup = false
        }
    }
}

private const val TAG = "IntentProcessor"
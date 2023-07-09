package com.deslomator.tagtimer

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.core.net.toUri
import androidx.core.util.Consumer
import androidx.documentfile.provider.DocumentFile
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.type.Result
import com.deslomator.tagtimer.navigation.AppNavHost
import com.deslomator.tagtimer.ui.theme.TagTimerTheme
import com.deslomator.tagtimer.util.restoreBackup
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltAndroidApp
class TagTimerApp: Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var appDao: AppDao

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            /*
            [intentState] receives the intent on cold launch and
            causes the import backup logic to run if the app
            is cold launched  from a file manager (f.e.);
            then it's set to null.
            Recreating the activity does not change its value even
            when the same intent is received after a configuration change.
            From this point only OnNewIntentListener is able to
            update it; this occurs when the app is already in the
            background and a supported file is clicked in other app.
            This is made possible by rememberSaveable.
            OnNewIntent is fired because the Activity is SingleInstance.
            [auxiliarUri} is not persisted, it received the value of
            intentState before the latter is nullified and allows the
            loadBackup logic to go on. It's eventually set to null when
            the activity is destroyed.
             */
            var intentState: Intent? by rememberSaveable { mutableStateOf(intent) }
            var auxiliarUri: Uri? = null
            DisposableEffect(Unit) {
                val listener = Consumer<Intent> {
                    intentState = it
                }
                addOnNewIntentListener(listener)
                onDispose { removeOnNewIntentListener(listener) }
            }
            TagTimerTheme {
                LaunchedEffect(Unit) {
                    withContext(Dispatchers.IO) { cleanOrphans(appDao) }
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost()
                    /*
                    The intent logic starts here
                     */
                    var showImportDialog by rememberSaveable { mutableStateOf(false) }
                    var loadBackup by remember { mutableStateOf(false) }
                    LaunchedEffect(intentState) {
                        intentState?.data?.let {
                            showImportDialog = true
                            auxiliarUri = it
                            intentState = null // only OnNewIntentListener can update it now
                        }
                    }
                    if (showImportDialog && auxiliarUri != null) {
                        AlertDialog(
                            onDismissRequest = { showImportDialog = false }
                        ) {
                            var filename by remember { mutableStateOf("") }
                            val documentFile = DocumentFile.fromSingleUri(this, auxiliarUri!!)
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
                                    Spacer(modifier = Modifier.height(30.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        TextButton(
                                            onClick = { showImportDialog = false }
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
                        var result = Result.RESTORE_FAILED
                        val snackState = remember { SnackbarHostState() }
                        val snackScope = rememberCoroutineScope()
                        LaunchedEffect(Unit) {
                            auxiliarUri?.let { intentUri ->
                                Log.d(TAG, "inside intentState?.data?.let")
                                withContext(Dispatchers.IO) {
                                    try {
                                        val inStream =
                                            contentResolver.openInputStream(intentUri)
                                        if (inStream != null) {
                                            val json = inStream.use {
                                                it.readBytes().decodeToString()
                                            }
                                            result = restoreBackup(appDao, json)
                                        } else {
                                            Log.e(
                                                TAG,
                                                "loadBackup: Unable to open Input Stream"
                                            )
                                        }
                                    } catch (error: Error) {
                                        Log.e(TAG, "loadBackup: $error")
                                    }
                                }
                            }
                            Log.d(TAG, "inside Unit  launched effect, result: ${result.name}")
                            snackScope.launch { snackState.showSnackbar(result.name) }
                        }
                        loadBackup = false
                    }
                }
            }
        }
    }
}

private const val TAG = "MainActivity"

package com.deslomator.tagtimer

import android.content.Intent
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.documentfile.provider.DocumentFile
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.type.Result
import com.deslomator.tagtimer.ui.theme.TagTimerTheme
import com.deslomator.tagtimer.util.restoreBackup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class LoadBackupActivity : ComponentActivity() {

    @Inject lateinit var appDao: AppDao

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TagTimerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var filename by remember { mutableStateOf("") }
                    var loadBackup by remember { mutableStateOf(false) }
                    val endActivity: () -> Unit = {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    intent.data?.let { intentUri ->
                        val documentFile = DocumentFile.fromSingleUri(this, intentUri)
                        filename = documentFile?.name.toString()
                        AlertDialog(
                            onDismissRequest = endActivity
                        ) {
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
                                        text = stringResource(id = R.string.load_backup, filename),
                                    )
                                    Spacer(modifier = Modifier.height(30.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        TextButton(
                                            onClick = endActivity
                                        ) {
                                            Text(text = stringResource(id = R.string.cancel))
                                        }
                                        TextButton(
                                            onClick = { loadBackup = true }
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
                            var result = Result.RESTORE_FAILED
                            intent.data?.let { intentUri ->
                                withContext(Dispatchers.IO) {
                                    try {
                                        val inStream = contentResolver.openInputStream(intentUri)
                                        if (inStream != null) {
                                            val json = inStream.use {
                                                it.readBytes().decodeToString()
                                            }
                                            result = restoreBackup(appDao, json)
                                        } else {
                                            Log.e(TAG, "LaunchedEffect. Unable to open Input Stream")
                                        }
                                    } catch (error: Error) {
                                        Log.e(TAG, "loadBackup: $error")
                                    }
                                }
                            }
                        }
                        endActivity()
                    }
                }
            }
        }
    }
}

private const val TAG = "LoadBackupActivity"
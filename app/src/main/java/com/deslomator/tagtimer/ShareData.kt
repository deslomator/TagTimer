package com.deslomator.tagtimer

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.FileProvider
import com.deslomator.tagtimer.model.type.Shared
import java.io.File

@Composable
fun ShareData(
    context: Context,
    fileName: String,
    data: String,
    onDataShared: () -> Unit,
    type: Shared = Shared.Csv
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {}
    LaunchedEffect(Unit) {
        try {
            val file = File(
                context.cacheDir,
                "$fileName.${type.extension}"
            )
            file.writeBytes(data.encodeToByteArray())
            val uri = FileProvider.getUriForFile(
                context,
                FILE_PROVIDER,
                file
            )
            val intent = Intent(Intent.ACTION_SEND)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .setType(type.mime)
                .putExtra(Intent.EXTRA_STREAM, uri)

            launcher.launch(intent)

            onDataShared()
        } catch (e: Exception) {
            Log.d(TAG, "ShareSession, error: $e")
        }
    }
}

private const val TAG = "ExportSession"
const val FILE_PROVIDER = "com.deslomator.tagtimer.fileprovider"

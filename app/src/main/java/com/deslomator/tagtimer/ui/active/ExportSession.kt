package com.deslomator.tagtimer.ui.active

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

@Composable
fun ExportSession(
    context: Context,
    fileName: String,
    data: String,
    onSessionExported: () -> Unit
) {
    LaunchedEffect(Unit) {
        try {
            val file = File(
                context.cacheDir,
                "$fileName.$JSON_EXTENSION"
            )
            file.writeBytes(data.encodeToByteArray())
            val uri = FileProvider.getUriForFile(
                context,
                FILE_PROVIDER,
                file
            )
            val intent = Intent(Intent.ACTION_SEND)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .setType("application/json")
                .putExtra(Intent.EXTRA_STREAM, uri)
            Intent.createChooser(intent, "Choose an App")
            ContextCompat.startActivity(context, intent, null)
            onSessionExported()
        } catch (error: Error) {
            Log.d(TAG, "ShareSession, error: $error")
        }
    }
}

private const val TAG = "ExportSession"
private const val JSON_EXTENSION = "json"
const val FILE_PROVIDER = "com.deslomator.tagtimer.fileprovider"

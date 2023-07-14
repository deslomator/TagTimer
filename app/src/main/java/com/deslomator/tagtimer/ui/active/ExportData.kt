package com.deslomator.tagtimer.ui.active

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.deslomator.tagtimer.model.type.Shared
import java.io.File

@Composable
fun ExportData(
    context: Context,
    fileName: String,
    data: String,
    onDataExported: () -> Unit,
    type: Shared = Shared.Csv
) {
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
            Intent.createChooser(intent, "Choose an App")
            ContextCompat.startActivity(context, intent, null)
            onDataExported()
        } catch (e: Exception) {
            Log.d(TAG, "ShareSession, error: $e")
        }
    }
}

private const val TAG = "ExportSession"
const val FILE_PROVIDER = "com.deslomator.tagtimer.fileprovider"

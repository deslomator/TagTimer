package com.deslomator.tagtimer.ui.active

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.deslomator.tagtimer.action.ActiveSessionAction
import com.deslomator.tagtimer.state.ActiveSessionState
import java.io.File

@Composable
fun ExportSession(
    context: Context,
    state: ActiveSessionState,
    onAction: (ActiveSessionAction) -> Unit
) {
    LaunchedEffect(Unit) {
        try {
            val file = File(
                context.cacheDir,
                "${state.currentSession.name}.$JSON_EXTENSION"
            )
            file.writeBytes(state.sessionToExport.encodeToByteArray())
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
            onAction(ActiveSessionAction.SessionExported)
        } catch (error: Error) {
            Log.d(TAG, "ShareSession, error: $error")
        }
    }
}

private const val TAG = "ExportSession"
private const val JSON_EXTENSION = "json"
private const val FILE_PROVIDER = "com.deslomator.tagtimer.fileprovider"

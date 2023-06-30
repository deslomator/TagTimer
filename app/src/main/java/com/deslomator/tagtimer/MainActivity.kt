package com.deslomator.tagtimer

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.Event
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.Tag
import com.deslomator.tagtimer.navigation.AppNavHost
import com.deslomator.tagtimer.ui.theme.TagTimerTheme
import com.deslomator.tagtimer.ui.theme.colorPickerColors
import com.deslomator.tagtimer.viewmodel.ActiveSessionViewModel
import com.deslomator.tagtimer.viewmodel.LabelsTabViewModel
import com.deslomator.tagtimer.viewmodel.SessionsTabViewModel
import com.deslomator.tagtimer.viewmodel.TrashTabViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltAndroidApp
class TagTimerApp: Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var appDao: AppDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TagTimerTheme {
                LaunchedEffect(Unit) {
                    withContext(Dispatchers.IO) { cleanOrphans(appDao) }
//                    populateDb(appDao)
                }
                val sessionsTabViewModel = viewModel<SessionsTabViewModel>()
                val labelsTabViewModel = viewModel<LabelsTabViewModel>()
                val trashTabViewModel = viewModel<TrashTabViewModel>()
                val activeSessionViewModel = viewModel<ActiveSessionViewModel>()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val sessionsScreenState by sessionsTabViewModel.state.collectAsState()
                    val tagsScreenState by labelsTabViewModel.state.collectAsState()
                    val sessionsTrashState by trashTabViewModel.state.collectAsState()
                    val activeSessionState by activeSessionViewModel.state.collectAsState()
                    AppNavHost(
                        sessionsTabState = sessionsScreenState,
                        onSessionsAction = sessionsTabViewModel::onAction,
                        labelsTabState = tagsScreenState,
                        onTagsAction = labelsTabViewModel::onAction,
                        trashTabState = sessionsTrashState,
                        onSessionsTrashAction = trashTabViewModel::onAction,
                        activeSessionState = activeSessionState,
                        onActiveSessionAction = activeSessionViewModel::onAction,
                    )
                }
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}

private suspend fun cleanOrphans(dao: AppDao) {
    delay(4000)
    val oe = dao.clearOrphanEvents()
    val out = dao.clearOrphanPreSelectedTags()
    Log.d(TAG, "dao.clearOrphanEvents(): $oe")
    Log.d(TAG, "dao.clearOrphanUsedTags(): $out")
}

private suspend fun populateDb (dao: AppDao) {
    colorPickerColors.forEachIndexed { index, it ->
        Log.d(TAG, "populateDb() creating sessions andd tags")
        val i = index + 100
        val session = Session(
            id = i,
            color = it.toArgb(),
            name = "Session ${it.toArgb()}"
        )
        dao.upsertSession(session)
        val sessionT = session.copy(
            id = i + 200,
            inTrash = true
        )
        dao.upsertSession(sessionT)
        val tag = Tag(
            id = i,
            color = it.toArgb(),
            label = "Label ${it.toArgb()}",
        )
        dao.upsertTag(tag)
        val tagT = tag.copy(
            id = i + 200,
            inTrash = true
        )
        dao.upsertTag(tagT)
        repeat(colorPickerColors.size) {
            val event = Event(
                sessionId = i,
                elapsedTimeMillis = System.currentTimeMillis(),
                note = "lskflsflslsjlsfl",
                label = "event label: $i",
            )
            dao.upsertEvent(event)
            val eventT = event.copy(
                sessionId = i + 200,
                inTrash = true
            )
            dao.upsertEvent(eventT)
        }
    }
}

private const val TAG = "MainActivity"
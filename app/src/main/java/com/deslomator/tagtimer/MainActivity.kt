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
import com.deslomator.tagtimer.ui.main.AppNavHost
import com.deslomator.tagtimer.ui.theme.TagTimerTheme
import com.deslomator.tagtimer.ui.theme.brightness
import com.deslomator.tagtimer.ui.theme.colorPickerColors
import com.deslomator.tagtimer.viewmodel.ActiveSessionViewModel
import com.deslomator.tagtimer.viewmodel.SessionsScreenViewModel
import com.deslomator.tagtimer.viewmodel.SessionsTrashViewModel
import com.deslomator.tagtimer.viewmodel.TagsScreenViewModel
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
                val sessionsScreenViewModel = viewModel<SessionsScreenViewModel>()
                val tagsScreenViewModel = viewModel<TagsScreenViewModel>()
                val sessionsTrashViewModel = viewModel<SessionsTrashViewModel>()
                val activeSessionViewModel = viewModel<ActiveSessionViewModel>()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val sessionsScreenState by sessionsScreenViewModel.state.collectAsState()
                    val tagsScreenState by tagsScreenViewModel.state.collectAsState()
                    val sessionsTrashState by sessionsTrashViewModel.state.collectAsState()
                    val activeSessionState by activeSessionViewModel.state.collectAsState()
                    AppNavHost(
                        sessionsScreenState = sessionsScreenState,
                        onSessionsAction = sessionsScreenViewModel::onAction,
                        tagsScreenState = tagsScreenState,
                        onTagsAction = tagsScreenViewModel::onAction,
                        sessionsTrashState = sessionsTrashState,
                        onSessionsTrashAction = sessionsTrashViewModel::onAction,
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
    Log.d("MainActivity", "dao.clearOrphanEvents(): $oe")
    Log.d("MainActivity", "dao.clearOrphanUsedTags(): $out")
}

private suspend fun populateDb (dao: AppDao) {
    colorPickerColors.forEachIndexed { index, it ->
        val i = index + 100
        val iT = index + 200
        val session = Session(
            id = i,
            color = it.toArgb(),
            name = "Session ${it.toArgb()}"
        )
        dao.upsertSession(session)
        val sessionT = Session(
            id = iT,
            color = it.toArgb(),
            name = "Session ${it.toArgb()}",
            inTrash = true
        )
        dao.upsertSession(sessionT)
        val tag = Tag(
            id = i,
            color = it.toArgb(),
            label = "Label ${it.toArgb()}",
            category = "Category ${it.brightness()}",
        )
        dao.upsertTag(tag)
        val tagT = Tag(
            id = iT,
            color = it.toArgb(),
            label = "Label ${it.toArgb()}",
            category = "Category ${it.brightness()}",
            inTrash = true
        )
        dao.upsertTag(tagT)
        colorPickerColors.forEach { item ->
            val event = Event(
                sessionId = i,
                timestampMillis = System.currentTimeMillis(),
                note = "lskflsflslsjlsfl",
                category = "cat cata cat acat",
                label = "event label: $i",
            )
            dao.upsertEvent(event)
            val eventT = Event(
                sessionId = i,
                timestampMillis = System.currentTimeMillis(),
                note = "lskflsflslsjlsfl",
                category = "cat cata cat acat",
                label = "event label: $i",
                inTrash = true
            )
            dao.upsertEvent(eventT)
        }
    }
}
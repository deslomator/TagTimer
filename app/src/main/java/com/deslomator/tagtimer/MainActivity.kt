package com.deslomator.tagtimer

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.dao.SessionsDatabase
import com.deslomator.tagtimer.ui.main.AppNavHost
import com.deslomator.tagtimer.ui.theme.TagTimerTheme
import com.deslomator.tagtimer.viewmodel.SessionsScreenViewModel
import com.deslomator.tagtimer.viewmodel.SessionsTrashViewModel
import com.deslomator.tagtimer.viewmodel.TagsScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltAndroidApp
class TagTimerApp: Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TagTimerTheme {
                val db by lazy {
                    Room.databaseBuilder(
                        applicationContext,
                        SessionsDatabase::class.java,
                        "sessions.db"
                    ).build()
                }
//                Log.d(TAG, "before launched effect")
                LaunchedEffect(Unit) {
                    withContext(Dispatchers.IO) { cleanOrphans(db.appDao) }
                }
                val sessionsScreenViewModel = viewModel<SessionsScreenViewModel>()
                val tagsScreenViewModel = viewModel<TagsScreenViewModel>()
                val sessionsTrashViewModel = viewModel<SessionsTrashViewModel>()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val sessionsScreenState by sessionsScreenViewModel.state.collectAsState()
                    val tagsScreenState by tagsScreenViewModel.state.collectAsState()
                    val sessionsTrashState by sessionsTrashViewModel.state.collectAsState()
                    AppNavHost(
                        sessionsScreenState = sessionsScreenState,
                        onSessionsAction = sessionsScreenViewModel::onAction,
                        tagsScreenState = tagsScreenState,
                        onTagsAction = tagsScreenViewModel::onAction,
                        sessionsTrashState = sessionsTrashState,
                        onSessionsTrashAction = sessionsTrashViewModel::onAction
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
    dao.clearOrphanEvents()
    dao.clearOrphanUsedTags()
//    Log.d("MainActivity", "after clean orphans")
}
